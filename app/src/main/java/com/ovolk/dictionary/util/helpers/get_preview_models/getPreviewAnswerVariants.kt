package com.ovolk.dictionary.util.helpers.get_preview_models

import com.ovolk.dictionary.domain.model.exam.ExamAnswerVariant

fun getPreviewAnswerVariants():List<ExamAnswerVariant> {
    return listOf(
        ExamAnswerVariant(
            id = 0L,
            value = "Груша",
        ),
        ExamAnswerVariant(
            id = 0L,
            value = "Олівець",
        ),
        ExamAnswerVariant(
            id = 0L,
            value = "М'яч",
        ),
        ExamAnswerVariant(
            id = 0L,
            value = "Канат",
            initialIsSelected = true
        ),
        ExamAnswerVariant(
            id = 0L,
            value = "Дурень",
        ),
        ExamAnswerVariant(
            id = 0L,
            value = "Пиво",
        ),
    )
}