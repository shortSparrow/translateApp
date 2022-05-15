package com.example.ttanslateapp.domain.use_case

import com.example.ttanslateapp.data.mapper.WordMapper
import com.example.ttanslateapp.domain.ExamWordAnswerRepository
import com.example.ttanslateapp.domain.TranslatedWordRepository
import com.example.ttanslateapp.domain.model.exam.ExamAnswerVariant
import com.example.ttanslateapp.util.temporarryAnswerList
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import timber.log.Timber
import javax.inject.Inject

class GetExamWordListUseCase @Inject constructor(
    private val repository: TranslatedWordRepository,
    private val examWordAnswerRepository: ExamWordAnswerRepository,
    val mapper: WordMapper,
) {
    suspend operator fun invoke(count: Int = EXAM_WORD_LIST_COUNT) = coroutineScope {
        val answerList = getAnswerList(count)
        repository.getExamWordList(count)
            .mapIndexed { index, examWord -> examWord.copy(answerVariants = answerList.slice((index * EXAM_WORD_ANSWER_LIST_SIZE)..EXAM_WORD_ANSWER_LIST_SIZE)) }
    }


    private suspend fun getAnswerList(examWordListCount: Int) = coroutineScope {
        async {
            val limit = examWordListCount * EXAM_WORD_ANSWER_LIST_SIZE
            val list = examWordAnswerRepository.getWordAnswerList(limit)
            if (list.isEmpty()) {
                for (wordItem in temporarryAnswerList) {
                    val word = ExamAnswerVariant(
                        value = wordItem,
                    )
                    val dbWord = mapper.examAnswerToExamAnswerDb(word)
                    examWordAnswerRepository.modifyWordAnswer(dbWord)
                }
                examWordAnswerRepository.getWordAnswerList(limit).shuffled()
            } else {
                list.shuffled()
            }

        }
    }.await()

    companion object {
        private const val EXAM_WORD_LIST_COUNT = 4
        private const val EXAM_WORD_ANSWER_LIST_SIZE = 6
    }
}