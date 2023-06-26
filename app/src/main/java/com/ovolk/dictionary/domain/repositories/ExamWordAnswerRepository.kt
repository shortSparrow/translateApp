package com.ovolk.dictionary.domain.repositories

import com.ovolk.dictionary.data.model.PotentialExamAnswerDb
import com.ovolk.dictionary.domain.model.exam.ExamAnswerVariant

interface ExamWordAnswerRepository {
    suspend fun getWordAnswerList(limit: Int): List<ExamAnswerVariant>
    suspend fun modifyWordAnswer(potentialExamAnswerDb: PotentialExamAnswerDb): Boolean
    suspend fun setWordAnswerList(list: List<ExamAnswerVariant>)
}