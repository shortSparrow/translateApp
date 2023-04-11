package com.ovolk.dictionary.domain.use_case.exam

import com.ovolk.dictionary.data.mapper.WordMapper
import com.ovolk.dictionary.domain.ExamWordAnswerRepository
import com.ovolk.dictionary.domain.TranslatedWordRepository
import com.ovolk.dictionary.domain.model.exam.ExamAnswerVariant
import com.ovolk.dictionary.domain.model.exam.ExamWord
import com.ovolk.dictionary.domain.use_case.daily_exam_settings.HandleDailyExamSettingsUseCase
import com.ovolk.dictionary.presentation.exam.ExamMode
import com.ovolk.dictionary.presentation.exam.ExamMode.*
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
) {
    private var getExamWordListCurrentPage: Int = 0
    private var isLoadingNextPage: Boolean = false
    private var totalCount: Int = 0
    private val dailyExamWordsCount =
        handleDailyExamSettingsUseCase.getDailyExamSettings().countOfWords.toInt()

    suspend fun searchWordListSize() = coroutineScope { repository.searchWordListSize() }

    suspend fun loadNextPage(listId: Long?, mode: ExamMode): ExamWordListUseCaseResponse? {
        if (isLoadingNextPage) return null
        return invoke(listId = listId, mode = mode)
    }

    suspend operator fun invoke(isInitialLoad: Boolean = false, listId: Long?, mode: ExamMode) =
        coroutineScope {
            isLoadingNextPage = true
            if (isInitialLoad) {
                loadTotalCount(listId, mode)
                getExamWordListCurrentPage = 0
            }
            val skip = getExamWordListCurrentPage * dailyExamWordsCount
            getExamWordListCurrentPage += 1

            val answerList = getExamAnswerVariants(dailyExamWordsCount)

            val request = if (listId != null) {
                repository.getExamWordListFromOneList(
                    count = dailyExamWordsCount,
                    skip = skip,
                    listId = listId
                )
            } else {
                repository.getExamWordList(
                    count = dailyExamWordsCount,
                    skip = skip,
                )
            }

            val examWordList = request.shuffled().mapIndexed { index, examWord ->
                val from = index * EXAM_WORD_ANSWER_LIST_SIZE
                val to = from + EXAM_WORD_ANSWER_LIST_SIZE - 1

                val randomWordTranslateIndex =
                    Random(System.currentTimeMillis()).nextInt(0 until examWord.translates.size)

                // only for available languages for this feature
                if (showVariantsAvailableLanguages.contains(examWord.langTo.uppercase())) {
                    examWord.answerVariants = answerList.slice(from until to)
                        .plus(ExamAnswerVariant(value = examWord.translates[randomWordTranslateIndex].value))
                        .shuffled()
                        .toMutableList()
                }

                examWord
            }

            ExamWordListUseCaseResponse(
                examWordList,
                totalCount,
            )
                .apply { isLoadingNextPage = false }
        }

    private fun loadTotalCount(listId: Long?, mode: ExamMode) {
        CoroutineScope(Dispatchers.IO).launch {
            val tempTotalCount = if (listId == null) {
                repository.getExamWordListSize()
            } else {
                repository.getExamWordListSizeForOneList(listId)
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