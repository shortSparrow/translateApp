package com.ovolk.dictionary.domain.use_case.select_languages

import com.ovolk.dictionary.presentation.select_languages.Language
import javax.inject.Inject

class SearchLanguageList @Inject constructor() {
    operator fun invoke(languageList: List<Language>, query: String): List<Language> {
        return languageList.filter {
            it.nativeName.lowercase()
                .contains(query.lowercase()) || it.name.lowercase()
                .contains(query.lowercase())
        }
    }
}