package com.ovolk.dictionary.domain.model.select_languages

data class Language(
    val langCode: String,
    val name: String,
    val nativeName: String,
    val isChecked: Boolean = false
)
