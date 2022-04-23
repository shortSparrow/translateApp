package com.example.ttanslateapp.domain

data class Word(
    val id: Long,
    val value: String,
    val translations: ArrayList<TranslationItem>,
    val description: String,
    val sound: Any, // english sound
)
