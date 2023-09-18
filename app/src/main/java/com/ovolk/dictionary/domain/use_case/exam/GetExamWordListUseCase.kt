package com.ovolk.dictionary.domain.use_case.exam

import android.app.Application
import com.ovolk.dictionary.R
import com.ovolk.dictionary.data.mapper.WordMapper
import com.ovolk.dictionary.domain.model.exam.ExamAnswerVariant
import com.ovolk.dictionary.domain.model.exam.ExamWord
import com.ovolk.dictionary.domain.model.modify_word.modify_word_chip.Translate
import com.ovolk.dictionary.domain.repositories.AppSettingsRepository
import com.ovolk.dictionary.domain.repositories.ExamWordAnswerRepository
import com.ovolk.dictionary.domain.repositories.TranslatedWordRepository
import com.ovolk.dictionary.domain.response.Either
import com.ovolk.dictionary.domain.response.FailureMessage
import com.ovolk.dictionary.domain.use_case.daily_exam_settings.HandleDailyExamSettingsUseCase
import com.ovolk.dictionary.presentation.exam.ExamMode
import com.ovolk.dictionary.presentation.exam.ExamMode.DAILY_MODE
import com.ovolk.dictionary.presentation.exam.ExamMode.INFINITY_MODE
import com.ovolk.dictionary.util.EXAM_WORD_ANSWER_LIST_SIZE
import com.ovolk.dictionary.util.showVariantsAvailableLanguages
import com.ovolk.dictionary.util.temporarryAnswerList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random
import kotlin.random.nextInt

data class ExamWordListUseCaseResponse(
    val examWordList: List<ExamWord>,
    val totalCount: Int
)

class GetExamWordListUseCase @Inject constructor(
    private val repository: TranslatedWordRepository,
    private val examWordAnswerRepository: ExamWordAnswerRepository,
    handleDailyExamSettingsUseCase: HandleDailyExamSettingsUseCase,
    val mapper: WordMapper,
    private val application: Application,
    appSettingsRepository: AppSettingsRepository,
) {
    val isDoubleLanguageExamEnable =
        appSettingsRepository.getAppSettings().isDoubleLanguageExamEnable
    private var getExamWordListCurrentPage: Int = 0
    private var isLoadingNextPage: Boolean = false
    private var totalCount: Int = 0
    private val dailyExamWordsCount =
        handleDailyExamSettingsUseCase.getDailyExamSettings().countOfWords.toInt()

    suspend fun searchWordListSize(dictionaryId: Long?) = coroutineScope {
        if (dictionaryId != null) {
            repository.searchWordListSizeByDictionary(dictionaryId = dictionaryId)
        } else {
            repository.searchWordListSize()
        }
    }

    suspend fun loadNextPage(
        listId: Long?,
        mode: ExamMode,
        dictionaryId: Long?
    ): Either<ExamWordListUseCaseResponse, FailureMessage>? {
        if (isLoadingNextPage) return null
        return invoke(false, listId = listId, mode = mode, dictionaryId = dictionaryId)
    }

    suspend operator fun invoke(
        isInitialLoad: Boolean = false,
        listId: Long?,
        mode: ExamMode,
        dictionaryId: Long?
    ): Either<ExamWordListUseCaseResponse, FailureMessage> =
        coroutineScope {
            if (dictionaryId == null) {
                return@coroutineScope Either.Failure(FailureMessage(application.getString(R.string.get_exam_word_list_use_case_no_dictionary_id)))
            }

            isLoadingNextPage = true
            if (isInitialLoad) {
                loadTotalCount(listId = listId, mode = mode, dictionaryId = dictionaryId)
                getExamWordListCurrentPage = 0
            }
            val skip = getExamWordListCurrentPage * dailyExamWordsCount
            getExamWordListCurrentPage += 1

            val answerList = getExamAnswerVariants(dailyExamWordsCount)

            val request = if (listId != null) {
                repository.getExamWordListFromOneList(
                    count = dailyExamWordsCount,
                    skip = skip,
                    listId = listId,
                    dictionaryId = dictionaryId,
                )
            } else {
                repository.getExamWordList(
                    count = dailyExamWordsCount,
                    skip = skip,
                    dictionaryId = dictionaryId,
                )
            }

            val examWordList = request.shuffled().mapIndexed { index, tempExamWord ->
                val from = index * EXAM_WORD_ANSWER_LIST_SIZE
                val to = from + EXAM_WORD_ANSWER_LIST_SIZE - 1


                val shouldInvertWord = Random.nextInt(0 until 2)
                val examWord = if (isDoubleLanguageExamEnable && shouldInvertWord == 1) {
                    val randomInverseWordTranslateIndex =
                        Random(System.currentTimeMillis()).nextInt(0 until tempExamWord.translates.size)
                    tempExamWord.copy(
                        value = tempExamWord.translates[randomInverseWordTranslateIndex].value,
                        initialTranslates = listOf(
                            Translate(
                                value = tempExamWord.value,
                                id = 0L,
                                localId = 0L,
                                createdAt = 0L,
                                updatedAt = 0L,
                                isHidden = false,
                            )
                        ),
                        langTo = tempExamWord.langFrom,
                        langFrom = tempExamWord.langTo,
                        isInverseWord = true,
                        inverseValueList = tempExamWord.translates.map { it.value }
                    )
                } else {
                    tempExamWord
                }

                val randomWordTranslateIndex =
                    Random(System.currentTimeMillis()).nextInt(0 until examWord.translates.size)

                // // only for available languages for this feature
                if (showVariantsAvailableLanguages.contains(examWord.langTo.uppercase())) {
                    examWord.answerVariants = answerList.slice(from until to)
                        .plus(ExamAnswerVariant(value = examWord.translates[randomWordTranslateIndex].value))
                        .shuffled()
                        .toMutableList()
                }

                return@mapIndexed examWord
            }

            Either.Success(
                ExamWordListUseCaseResponse(
                    examWordList,
                    totalCount,
                )
                    .apply { isLoadingNextPage = false }
            )
        }

    private fun loadTotalCount(listId: Long?, mode: ExamMode, dictionaryId: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            val tempTotalCount = if (listId == null) {
                repository.getExamWordListSize(dictionaryId = dictionaryId)
            } else {
                repository.getExamWordListSizeForOneList(
                    listId = listId,
                    dictionaryId = dictionaryId
                )
            }

            totalCount = when (mode) {
                DAILY_MODE -> minOf(tempTotalCount, dailyExamWordsCount)
                INFINITY_MODE -> tempTotalCount
            }
        }
    }

    // get 60 random Answer Variants
    private suspend fun getExamAnswerVariants(examWordListCount: Int) = coroutineScope {
        val limit = examWordListCount * EXAM_WORD_ANSWER_LIST_SIZE
        val list = examWordAnswerRepository.getWordAnswerList(limit)

        if (list.isEmpty()) {
            fillAnswerVariantsDb()
            examWordAnswerRepository.getWordAnswerList(limit)
        } else {
            list
        }
    }

    private suspend fun fillAnswerVariantsDb() {
        val newList = mutableListOf<ExamAnswerVariant>()
        for (wordItem in temporarryAnswerList) {
            newList.add(
                ExamAnswerVariant(
                    value = wordItem,
                )
            )
        }

        examWordAnswerRepository.setWordAnswerList(newList)
    }

}