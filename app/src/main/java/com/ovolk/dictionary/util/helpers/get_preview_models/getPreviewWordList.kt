package com.ovolk.dictionary.util.helpers.get_preview_models

import com.ovolk.dictionary.domain.model.modify_word.WordAudio
import com.ovolk.dictionary.domain.model.modify_word.WordRV

fun getPreviewWordList()= listOf(
    WordRV(
        id = 0L,
        value = "grey",
        translates = getPreviewTranslates(),
        description = "",
        sound = null,
        langFrom = "EN",
        langTo = "UA",
        transcription = ""
    ),
    WordRV(
        id = 1L,
        value = "miscommunication",
        translates = getPreviewTranslates(),
        description = "",
        sound = WordAudio(fileName = ""),
        langFrom = "EN",
        langTo = "UA",
        transcription = ""
    ),
    WordRV(
        id = 1L,
        value = "voice",
        translates = getPreviewTranslates(),
        description = "any sound of human or device",
        sound = WordAudio(fileName = ""),
        langFrom = "EN",
        langTo = "UA",
        transcription = "войс"
    )
)