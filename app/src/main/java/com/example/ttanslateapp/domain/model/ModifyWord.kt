package com.example.ttanslateapp.domain.model

import com.example.ttanslateapp.data.model.Sound
import com.example.ttanslateapp.domain.model.modify_word_chip.HintItem
import com.example.ttanslateapp.domain.model.modify_word_chip.TranslateWordItem
import javax.inject.Inject

data class ModifyWord @Inject constructor(
    val id: Long = UNDEFINED_ID,
    val value: String,
    val translates: List<TranslateWordItem>,
    val description: String,
    val sound: Sound?, // english sound
    val langFrom: String,
    val langTo: String,
    val hints: List<HintItem>?,
    val transcription: String
) {
    companion object {
        const val UNDEFINED_ID = 0L
    }
}
