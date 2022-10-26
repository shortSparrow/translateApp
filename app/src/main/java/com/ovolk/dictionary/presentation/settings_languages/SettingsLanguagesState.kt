package com.ovolk.dictionary.presentation.settings_languages

import com.ovolk.dictionary.domain.model.select_languages.Language
import com.ovolk.dictionary.domain.model.select_languages.LanguagesType

data class SettingsLanguagesState(
    val languagesTo: List<Language> = emptyList(),
    val languagesFrom: List<Language> = emptyList(),
)

sealed class SettingsLanguagesAction {
    data class EditSelectedLanguages(val type: LanguagesType) : SettingsLanguagesAction()
    object OnFocusScreen: SettingsLanguagesAction()
}