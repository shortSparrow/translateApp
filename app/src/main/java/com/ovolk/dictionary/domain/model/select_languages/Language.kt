package com.ovolk.dictionary.domain.model.select_languages

data class Language(
    val langCode: String,
    val name: String,
    val nativeName: String,
    val isChecked: Boolean = false
)

// TODO remove
data class SharedLanguage(
    val langCode: String,
)

enum class LanguagesType { LANG_TO, LANG_FROM }
