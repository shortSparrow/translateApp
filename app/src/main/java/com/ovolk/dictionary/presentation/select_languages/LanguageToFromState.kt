package com.ovolk.dictionary.presentation.select_languages

import com.ovolk.dictionary.domain.model.select_languages.Language
import com.ovolk.dictionary.domain.model.select_languages.LanguagesType


data class LanguageToFromState(
    val languageList: List<Language> = emptyList(),
    val filteredLanguageList: List<Language> = emptyList(),
    val preferredLanguages: List<Language> = emptyList(),
    val type: LanguagesType? = null,
    val headerWithBackButton:Boolean = false
)

sealed class LanguagesToFromActions {
    data class ToggleCheck(val language: Language) : LanguagesToFromActions()
    data class OnSearchLanguagesTo(val query: String) : LanguagesToFromActions()
    object GoNext : LanguagesToFromActions()
}