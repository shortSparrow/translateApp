package com.example.ttanslateapp.domain

data class ModifyWord(
    val id: Long,
    val value: String,
    val translations: ArrayList<TranslationItem>,
    val description: String,
    val sound: Any, // english sound
    val langFrom: String,
    val langTo: String,
    val hintList: ArrayList<HintItem>,
    val answerList: ArrayList<AnswerItem>
)
