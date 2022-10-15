package com.ovolk.dictionary.presentation.select_languages

data class Language(
    val langCode: String,
    val name: String,
    val nativeName: String,
    val isChecked: Boolean = false
)

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