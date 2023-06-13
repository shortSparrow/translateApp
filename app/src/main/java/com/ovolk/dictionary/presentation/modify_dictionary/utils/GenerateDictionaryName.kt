package com.ovolk.dictionary.presentation.modify_dictionary.utils

fun generateDictionaryNameLangFrom(name: String, langCode: String): String {
    var dictionaryName = name
    val dictionaryNameParsed = name.split("-").toMutableList()
    if (dictionaryNameParsed.size == 2) {
        dictionaryNameParsed[0] = langCode.uppercase().trim()
        dictionaryNameParsed[1] = dictionaryNameParsed[1].trim()
        dictionaryName = dictionaryNameParsed.joinToString(" - ")
    } else if (name.isEmpty()) {
        dictionaryName = "$langCode -".uppercase()
    }

    return dictionaryName
}

fun generateDictionaryNameLangTo(name: String, langCode: String): String {
    var dictionaryName = name
    val dictionaryNameParsed = name.split("-").toMutableList()
    if (dictionaryNameParsed.size == 2) {
        dictionaryNameParsed[0] = dictionaryNameParsed[0].trim()
        dictionaryNameParsed[1] = langCode.uppercase().trim()
        dictionaryName = dictionaryNameParsed.joinToString(" - ")
    } else if (name.isEmpty()) {
        dictionaryName = " - $langCode".uppercase()
    }

    return dictionaryName
}
