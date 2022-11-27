package com.ovolk.dictionary.util.helpers.get_preview_models

import com.ovolk.dictionary.domain.model.modify_word.modify_word_chip.HintItem


fun getPreviewHints(): List<HintItem> = listOf(
    HintItem(
        id = 0L,
        localId = 0L,
        createdAt = System.currentTimeMillis(),
        updatedAt = System.currentTimeMillis(),
        value = "a fruit",
    ),
    HintItem(
        id = 1L,
        localId = 1L,
        createdAt = System.currentTimeMillis(),
        updatedAt = System.currentTimeMillis(),
        value = "has red color",
    ),
    HintItem(
        id = 2L,
        localId = 2L,
        createdAt = System.currentTimeMillis(),
        updatedAt = System.currentTimeMillis(),
        value = "many people like it",
    )
)