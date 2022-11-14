package com.ovolk.dictionary.util.helpers.get_preview_models

import com.ovolk.dictionary.domain.model.exam.ExamAnswerVariant
import com.ovolk.dictionary.domain.model.exam.ExamWord
import com.ovolk.dictionary.domain.model.exam.ExamWordStatus
import com.ovolk.dictionary.presentation.list_full.components.getPreviewTranslates

fun getPreviewExamListAllStatus() = listOf(
    ExamWord(
        id = 0L,
        value = "Apple",
        translates = getPreviewTranslates(),
        hints = getPreviewHints(),
        initialPriority = 5,
        initialStatus = ExamWordStatus.IN_PROCESS,
        answerVariants = mutableListOf(),
        isShowVariantsAvailable = true
    ),
    ExamWord(
        id = 1L,
        value = "Fish",
        translates = getPreviewTranslates(),
        hints = getPreviewHints(),
        initialPriority = 5,
        initialStatus = ExamWordStatus.SUCCESS,
        answerVariants = mutableListOf(),
        isShowVariantsAvailable = false
    ),
    ExamWord(
        id = 2L,
        value = "Meat",
        translates = getPreviewTranslates(),
        hints = getPreviewHints(),
        initialPriority = 5,
        initialStatus = ExamWordStatus.FAIL,
        answerVariants = mutableListOf(ExamAnswerVariant(id = 0L, value = "ddd")),
    ),
    ExamWord(
        id = 3L,
        value = "green",
        translates = getPreviewTranslates(),
        hints = getPreviewHints(),
        initialPriority = 5,
        initialStatus = ExamWordStatus.UNPROCESSED,
        answerVariants = mutableListOf(ExamAnswerVariant(id = 0L, value = "ddd")),
    ),
)