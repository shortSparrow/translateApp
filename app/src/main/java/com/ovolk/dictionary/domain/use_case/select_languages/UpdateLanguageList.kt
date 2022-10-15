package com.ovolk.dictionary.domain.use_case.select_languages

import com.ovolk.dictionary.presentation.select_languages.Language
import javax.inject.Inject

class UpdateLanguageList @Inject constructor() {
    operator fun invoke(list: List<Language>, toggleLanguage: Language):List<Language> {
        val updatedLang = toggleLanguage.copy(isChecked = !toggleLanguage.isChecked)

        return list.map {
            return@map if (it.langCode == toggleLanguage.langCode) {
                updatedLang
            } else {
                it
            }
        }
    }
}