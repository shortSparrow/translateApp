package com.example.ttanslateapp.domain.model

import androidx.room.PrimaryKey

data class TranslatedWord(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val value: String,
    val translations: List<TranslationItem>,
    val description: String,
    val sound: Any?, // english sound
    val langFrom: String,
    val langTo: String,
    val hintList: List<HintItem?>,
    val answerList: List<AnswerItem?>
)
