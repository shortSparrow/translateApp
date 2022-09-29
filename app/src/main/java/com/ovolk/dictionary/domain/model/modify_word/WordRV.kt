package com.ovolk.dictionary.domain.model.modify_word

import com.ovolk.dictionary.domain.model.modify_word.modify_word_chip.Translate
import javax.inject.Inject

data class WordRV @Inject constructor(
    val id: Long,
    val value: String,
    val translates: List<Translate>,
    val description: String,
    val sound: WordAudio?, // english sound
    val langFrom: String,
    val langTo: String,
    val transcription: String,
)
