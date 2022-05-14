package com.example.ttanslateapp.domain.model.exam

import com.example.ttanslateapp.domain.model.modify_word_chip.HintItem
import com.example.ttanslateapp.domain.model.modify_word_chip.TranslateWordItem

data class ExamWord(
    val id: Long,
    val value: String,
    val translates: List<TranslateWordItem>,
    val hints: List<HintItem>,
    val priority: Int,

    val status: ExamWordStatus,
    val answerVariants:List<String> // FIXME maybe change String on smth more full
)

