package com.ovolk.dictionary.data.mapper

import com.ovolk.dictionary.data.model.PotentialExamAnswerDb
import com.ovolk.dictionary.data.model.WordFullDb
import com.ovolk.dictionary.domain.model.exam.ExamAnswerVariant
import com.ovolk.dictionary.domain.model.exam.ExamWord
import com.ovolk.dictionary.domain.model.exam.ExamWordStatus
import javax.inject.Inject

class ExamMapper @Inject constructor(private val wordMapper: WordMapper) {
    fun wordFullDbToExamWord(wordDb: WordFullDb): ExamWord =
        ExamWord(
            id = wordDb.wordInfo.id,
            value = wordDb.wordInfo.value,
            initialTranslates = wordDb.translates.map { wordMapper.translateDbToLocal(it) },
            hints = wordDb.hints.map { wordMapper.hintDbToLocal(it) },
            langTo = wordDb.dictionary.langToCode,
            langFrom = wordDb.dictionary.langFromCode,
            initialPriority = wordDb.wordInfo.priority,
            initialStatus = ExamWordStatus.UNPROCESSED,
            answerVariants = mutableListOf(),
        )


    fun examAnswerToExamAnswerDb(examAnswer: ExamAnswerVariant) = PotentialExamAnswerDb(
        id = examAnswer.id,
        value = examAnswer.value,
    )

    fun examAnswerDbToExamAnswer(examAnswer: PotentialExamAnswerDb) = ExamAnswerVariant(
        id = examAnswer.id,
        value = examAnswer.value,
    )

}