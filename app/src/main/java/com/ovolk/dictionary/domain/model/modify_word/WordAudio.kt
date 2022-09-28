package com.ovolk.dictionary.domain.model.modify_word

import com.ovolk.dictionary.data.model.Sound

data class WordAudio(
    override val fileName: String
) : Sound