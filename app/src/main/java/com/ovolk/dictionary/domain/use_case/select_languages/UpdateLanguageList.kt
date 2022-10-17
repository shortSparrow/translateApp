package com.ovolk.dictionary.domain.use_case.select_languages

import com.ovolk.dictionary.domain.model.select_languages.Language
import javax.inject.Inject

class UpdateLanguageList @Inject constructor() {
    operator fun invoke(list: List<Language>, toggledLanguage: Language):List<Language> {
        val updatedLang = toggledLanguage.copy(isChecked = !toggledLanguage.isChecked)

        return list.map {
            return@map if (it.langCode == toggledLanguage.langCode) {
                updatedLang
            } else {
                it
            }
        }
    }
}