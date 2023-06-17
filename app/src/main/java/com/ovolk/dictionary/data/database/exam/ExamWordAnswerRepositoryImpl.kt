package com.ovolk.dictionary.data.database.exam

import com.ovolk.dictionary.data.mapper.WordMapper
import com.ovolk.dictionary.data.model.PotentialExamAnswerDb
import com.ovolk.dictionary.domain.repositories.ExamWordAnswerRepository
import com.ovolk.dictionary.domain.model.exam.ExamAnswerVariant
import javax.inject.Inject

class ExamWordAnswerRepositoryImpl @Inject constructor(
    private val examWordAnswerDao: ExamWordAnswerDao,
    private val mapper: WordMapper
) : ExamWordAnswerRepository {
    override suspend fun getWordAnswerList(limit: Int): List<ExamAnswerVariant> {
        return examWordAnswerDao.getWordAnswerList(limit)
            .map { mapper.examAnswerDbToExamAnswer(it) }
    }

    override suspend fun modifyWordAnswer(potentialExamAnswerDb: PotentialExamAnswerDb): Boolean {
        return examWordAnswerDao.modifyWordAnswer(potentialExamAnswerDb)
            .toInt() != ANSWER_VARIANT_IS_NOT_FOUND
    }

    override suspend fun setWordAnswerList(list: List<ExamAnswerVariant>) {
        return examWordAnswerDao.setWordAnswerList(list.map { mapper.examAnswerToExamAnswerDb(it) })
    }

    companion object {
        private const val ANSWER_VARIANT_IS_NOT_FOUND = -1
    }
}