package com.example.ttanslateapp.domain.use_case.exam

import com.example.ttanslateapp.data.mapper.WordMapper
import com.example.ttanslateapp.domain.ExamWordAnswerRepository
import com.example.ttanslateapp.domain.TranslatedWordRepository
import com.example.ttanslateapp.domain.model.exam.ExamAnswerVariant
import com.example.ttanslateapp.domain.model.exam.ExamWord
import com.example.ttanslateapp.util.EXAM_WORD_ANSWER_LIST_SIZE
import com.example.ttanslateapp.util.temporarryAnswerList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.random.Random
import kotlin.random.nextInt

class GetExamWordListUseCase @Inject constructor(
    private val repository: TranslatedWordRepository,
    private val examWordAnswerRepository: ExamWordAnswerRepository,
    val mapper: WordMapper,
) {
    private var getExamWordListCurrentPage: Int = 0
    private var isLoadingNextPage: Boolean = false
    private var totalCount: Int = 0

    fun getTotalCountExamList() = totalCount


    fun resetExamWordListCurrentPage() {
        getExamWordListCurrentPage = 0
    }

    suspend fun searchWordListSize() = coroutineScope { repository.searchWordListSize() }

    suspend fun loadNextPage(listId: Long?): List<ExamWord>? {
        if (isLoadingNextPage) return null
        return invoke(listId = listId)
    }

    private fun loadTotalCount(listId: Long?) {
        CoroutineScope(Dispatchers.IO).launch {
            totalCount = if (listId == null) {
                repository.getExamWordListSize()
            } else {
                repository.getExamWordListSizeForOneList(listId)
            }

        }
    }

    suspend operator fun invoke(isInitialLoad: Boolean = false, listId: Long?) = coroutineScope {
        isLoadingNextPage = true
        val skip = getExamWordListCurrentPage * EXAM_WORD_LIST_COUNT
        getExamWordListCurrentPage += 1

        if (isInitialLoad) {
            loadTotalCount(listId)
        }
        val answerList = getExamAnswerVariants(EXAM_WORD_LIST_COUNT)

        val request = if (listId != null) {
            repository.getExamWordListFromOneList(
                count = EXAM_WORD_LIST_COUNT,
                skip = skip,
                listId = listId
            )
        } else {
            repository.getExamWordList(
                count = EXAM_WORD_LIST_COUNT,
                skip = skip,
            )
        }

        request.mapIndexed { index, examWord ->
            val from = index * EXAM_WORD_ANSWER_LIST_SIZE
            val to = from + EXAM_WORD_ANSWER_LIST_SIZE - 1

            val randomWordTranslateIndex =
                Random(System.currentTimeMillis()).nextInt(0 until examWord.translates.size)
            examWord.copy(
                answerVariants = answerList.slice(from until to)// FIXME ME FROM TO
                    .plus(ExamAnswerVariant(value = examWord.translates[randomWordTranslateIndex].value))
                    .shuffled()
            )
        }
            .apply { isLoadingNextPage = false }
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

    companion object {
        private const val EXAM_WORD_LIST_COUNT = 10
    }
}