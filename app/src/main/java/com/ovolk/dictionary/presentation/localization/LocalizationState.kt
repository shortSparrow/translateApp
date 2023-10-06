package com.ovolk.dictionary.presentation.localization

import com.ovolk.dictionary.domain.model.select_languages.Language


data class LocalizationState(
    val selectedLanguage: Language? = null,
    val languageFilteredList: List<Language> = emptyList(),
    val isLoading: Boolean = true,
    val isConfirmAppChangeLanguageModalOpen: Boolean = false,
    val isConfirmAppChangeLanguage: Language? = null,
)

data class LocalizationStatePrivate(
    val languageList: List<Language> = emptyList(),
)

sealed interface LocalizationAction {
    object OnPressGoBack : LocalizationAction
    data class OnSearchLanguage(val language: String) : LocalizationAction
    data class OnOpenConfirmChangeAppLanguageModal(val language: Language?) : LocalizationAction
    object OnCloseConfirmChangeAppLanguageModal : LocalizationAction
    data class OnConfirmChangeAppLanguage(val languageCode: String?) : LocalizationAction
}