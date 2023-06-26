package com.ovolk.dictionary.util.helpers.get_preview_models

import com.ovolk.dictionary.domain.model.select_languages.Language

fun getPreviewLanguagesList() =listOf(
    Language(
        langCode = "rm",
        name = "Romansh",
        nativeName = "rumantsch grischun"
    ),
    Language(
        langCode = "sd",
        name = "Sindhi",
        nativeName = "सिन्धी"
    ),
    Language(
        langCode = "uk",
        name = "Ukrainian",
        nativeName = "українська",
        isChecked = true
    ),
    Language(
        langCode = "en",
        name = "English",
        nativeName = "English"
    ),
    Language(
        langCode = "se",
        name = "Northern Sami",
        nativeName = "Davvisámegiella"
    ),
    Language(
        langCode = "sm",
        name = "Samoan",
        nativeName = "gagana faa Samoa"
    ),
)