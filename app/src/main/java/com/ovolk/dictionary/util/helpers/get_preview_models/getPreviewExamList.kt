package com.ovolk.dictionary.util.helpers.get_preview_models

import com.ovolk.dictionary.domain.model.exam.ExamAnswerVariant
import com.ovolk.dictionary.domain.model.exam.ExamWord
import com.ovolk.dictionary.domain.model.exam.ExamWordStatus

fun getPreviewExamListAllStatus() = listOf(
    ExamWord(
        id = 0L,
        value = "Apple",
        initialTranslates = getPreviewTranslates(),
        hints = getPreviewHints(),
        langTo = "UK",
        initialPriority = 5,
        initialStatus = ExamWordStatus.IN_PROCESS,
        answerVariants = mutableListOf(),
    ),
    ExamWord(
        id = 1L,
        value = "Fish",
        initialTranslates = getPreviewTranslates(),
        hints = getPreviewHints(),
        langTo = "UK",
        initialPriority = 5,
        initialStatus = ExamWordStatus.SUCCESS,
        answerVariants = mutableListOf(),
    ),
    ExamWord(
        id = 2L,
        value = "Meat",
        initialTranslates = getPreviewTranslates(),
        langTo = "UK",
        hints = getPreviewHints(),
        initialPriority = 5,
        initialStatus = ExamWordStatus.FAIL,
        answerVariants = mutableListOf(ExamAnswerVariant(id = 0L, value = "ddd")),
    ),
    ExamWord(
        id = 3L,
        value = "green",
        initialTranslates = getPreviewTranslates(),
        hints = getPreviewHints(),
        langTo = "UK",
        initialPriority = 5,
        initialStatus = ExamWordStatus.UNPROCESSED,
        answerVariants = mutableListOf(ExamAnswerVariant(id = 0L, value = "ddd")),
    ),
)