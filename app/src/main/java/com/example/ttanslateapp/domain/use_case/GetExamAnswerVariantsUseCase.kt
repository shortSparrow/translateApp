package com.example.ttanslateapp.domain.use_case

import com.example.ttanslateapp.data.mapper.WordMapper
import com.example.ttanslateapp.domain.ExamWordAnswerRepository
import com.example.ttanslateapp.domain.model.exam.ExamAnswerVariant
import com.example.ttanslateapp.util.EXAM_WORD_ANSWER_LIST_SIZE
import com.example.ttanslateapp.util.temporarryAnswerList
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class GetExamAnswerVariantsUseCase @Inject constructor(
    private val examWordAnswerRepository: ExamWordAnswerRepository,
    val mapper: WordMapper,
) {
    suspend operator fun invoke(examWordListCount: Int) = coroutineScope {
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
}