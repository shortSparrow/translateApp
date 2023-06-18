package com.ovolk.dictionary.domain.model.modify_word

import com.ovolk.dictionary.domain.model.dictionary.Dictionary
import com.ovolk.dictionary.domain.model.modify_word.modify_word_chip.HintItem
import com.ovolk.dictionary.domain.model.modify_word.modify_word_chip.Translate
import javax.inject.Inject

data class ModifyWord @Inject constructor(
    val id: Long = UNDEFINED_ID,
    val priority: Int = DEFAULT_PRIORITY,
    val value: String,
    val translates: List<Translate>,
    val description: String,
    val sound: WordAudio?, // english sound
    val hints: List<HintItem>,
    val transcription: String,
    val createdAt: Long,
    val updatedAt: Long,
    val wordListId: Long? = null,
    val dictionary: Dictionary
) {
    companion object {
        const val UNDEFINED_ID = 0L
        const val DEFAULT_PRIORITY = 5
    }
}
