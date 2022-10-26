package com.ovolk.dictionary.data.mapper

import com.ovolk.dictionary.domain.model.modify_word.SelectLanguage
import com.ovolk.dictionary.domain.model.select_languages.Language
import com.ovolk.dictionary.domain.model.select_languages.SharedLanguage
import javax.inject.Inject

class LanguageMapper @Inject constructor() {
    fun languageToSharedLang(language: Language): SharedLanguage = SharedLanguage(
        langCode = language.langCode
    )

    fun languageToSelectLanguage(language: Language): SelectLanguage = SelectLanguage(
        langCode = language.langCode,
        name = language.name,
        nativeName = language.nativeName,
        isChecked = language.isChecked
    )

}