package com.example.ttanslateapp.domain.model

data class WordRV(
    val id: Long,
    val value: String,
    val translations: List<TranslationItem>,
    val description: String,
    val sound: Any?, // english sound
)
