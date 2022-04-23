package com.example.ttanslateapp.domain.model

data class ModifyWord(
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
