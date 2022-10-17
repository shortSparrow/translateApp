package com.ovolk.dictionary.presentation.settings_languages_to_from

import com.ovolk.dictionary.domain.model.select_languages.Language

data class SettingsLanguagesToFromState(
    val languageList: List<Language> = emptyList(),
    val filteredLanguageList: List<Language> = emptyList(),
    val selectedLanguages: List<Language> = emptyList()
)

sealed class SettingsLanguagesToFromActions {
    data class ToggleCheck(val language: Language) : SettingsLanguagesToFromActions()
    data class OnSearchLanguagesToFrom(val query: String) : SettingsLanguagesToFromActions()
}