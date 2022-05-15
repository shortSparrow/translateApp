package com.example.ttanslateapp.domain.use_case

import com.example.ttanslateapp.data.mapper.WordMapper
import com.example.ttanslateapp.domain.ExamWordAnswerRepository
import com.example.ttanslateapp.domain.model.exam.ExamAnswerVariant
import com.example.ttanslateapp.util.temporarryAnswerList
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class GetExamAnswerVariantsUseCase @Inject constructor(
    private val examWordAnswerRepository: ExamWordAnswerRepository,
    val mapper: WordMapper,
) {
    // FIXME temporary solution. When back will be done, we will load AnswerList from there
//    suspend operator fun invoke() = coroutineScope {
//        async {
//            val list = examWordAnswerRepository.getWordAnswerList()
//            if (list.isEmpty()) {
//                for (wordItem in temporarryAnswerList) {
//                    val word = ExamAnswerVariant(
//                        value = wordItem,
//                    )
//                    val dbWord = mapper.examAnswerToExamAnswerDb(word)
//                    examWordAnswerRepository.modifyWordAnswer(dbWord)
//                }
//                examWordAnswerRepository.getWordAnswerList()
//            } else {
//                list
//            }
//
//        }
//    }.await()
}