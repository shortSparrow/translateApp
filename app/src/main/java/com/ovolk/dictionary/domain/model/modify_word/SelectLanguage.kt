package com.ovolk.dictionary.domain.model.modify_word

data class SelectLanguage(
    val langCode: String,
    val name: String,
    val nativeName: String,
    val isChecked: Boolean = false
)
