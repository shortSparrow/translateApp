package com.ovolk.dictionary.domain.model.modify_dictionary

data class Language(
    val langCode: String,
    val name: String,
    val nativeName: String,
)

//enum class LanguagesType { LANG_TO, LANG_FROM }