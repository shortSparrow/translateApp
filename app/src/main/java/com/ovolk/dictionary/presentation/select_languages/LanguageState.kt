package com.ovolk.dictionary.presentation.select_languages

import com.ovolk.dictionary.domain.model.select_languages.Language


data class LanguageState(
    val languageList: List<Language> = emptyList(),
    val filteredLanguageList: List<Language> = emptyList(),
    val preferredLanguages: List<Language> = emptyList()
)

sealed class LanguagesToActions {
    data class ToggleCheck(val language: Language) : LanguagesToActions()
    data class OnSearchLanguagesTo(val query: String) : LanguagesToActions()
    object NavigateToLanguagesToFrom : LanguagesToActions()
}

sealed class LanguagesFromActions {
    data class ToggleCheck(val language: Language) : LanguagesFromActions()
    data class OnSearchLanguages(val query: String) : LanguagesFromActions()
    object NavigateToLanguagesTo : LanguagesFromActions()
    object NavigateToHome : LanguagesFromActions()
}