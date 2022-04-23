package com.example.ttanslateapp.domain.model

import javax.inject.Inject

data class WordRV @Inject constructor(
    val id: Long,
    val value: String,
    val translations: List<TranslationItem>,
    val description: String,
    val sound: Any?, // english sound
)
