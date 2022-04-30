package com.example.ttanslateapp.domain.model

import com.example.ttanslateapp.data.model.Sound
import com.example.ttanslateapp.domain.model.edit.TranslateWordItem
import javax.inject.Inject

data class WordRV @Inject constructor(
    val id: Long,
    val value: String,
    val translations: List<TranslateWordItem>,
    val description: String,
    val sound: Sound?, // english sound
    val langFrom: String,
    val langTo: String,
)
