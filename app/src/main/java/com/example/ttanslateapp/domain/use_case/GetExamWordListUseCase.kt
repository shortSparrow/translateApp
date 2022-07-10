package com.example.ttanslateapp.domain.use_case

import android.util.Log
import com.example.ttanslateapp.data.mapper.WordMapper
import com.example.ttanslateapp.domain.ExamWordAnswerRepository
import com.example.ttanslateapp.domain.TranslatedWordRepository
import com.example.ttanslateapp.domain.model.exam.ExamAnswerVariant
import com.example.ttanslateapp.presentation.exam.adapter.ExamMode
import com.example.ttanslateapp.util.EXAM_WORD_ANSWER_LIST_SIZE
import com.example.ttanslateapp.util.temporarryAnswerList
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject
import kotlin.random.Random
import kotlin.random.nextInt

class GetExamWordListUseCase @Inject constructor(
    private val repository: TranslatedWordRepository,
    private val examWordAnswerRepository: ExamWordAnswerRepository,
    val mapper: WordMapper,
) {
    suspend operator fun invoke(mode: ExamMode) = coroutineScope {
        val count =
            if (mode == ExamMode.INFINITY_MODE) repository.getExamWordListSize() else EXAM_WORD_LIST_COUNT

        val answerList = getExamAnswerVariants(count)
        repository.getExamWordList(count = count, skip = 0)
            .mapIndexed { index, examWord ->
                val from = index * EXAM_WORD_ANSWER_LIST_SIZE
                val to = from + EXAM_WORD_ANSWER_LIST_SIZE - 1

                val randomWordTranslateIndex =
                    Random(System.currentTimeMillis()).nextInt(0 until examWord.translates.size)
                examWord.copy(
                    answerVariants = answerList.slice(from until to)
                        .plus(ExamAnswerVariant(value = examWord.translates[randomWordTranslateIndex].value))
                        .shuffled()
                )
            }
    }

    private suspend fun getExamAnswerVariants(examWordListCount: Int) = coroutineScope {
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
            examWordAnswerRepository.getWordAnswerList(limit)
        } else {
            list
        }
    }

    companion object {
        private const val EXAM_WORD_LIST_COUNT = 10
    }
}