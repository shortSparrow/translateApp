package com.ovolk.dictionary.presentation.settings_dictionaries

import com.ovolk.dictionary.domain.model.dictionary.Dictionary

data class SettingsDictionariesState(
    val dictionaryList: List<Dictionary> = emptyList(),
    val isDeleteDictionaryModalOpen: Boolean = false,
)

sealed interface SettingsDictionariesAction {
    data class OnSelectDictionary(val id: Long) : SettingsDictionariesAction
    data class OnPressDictionary(val id: Long) : SettingsDictionariesAction
    data class ToggleOpenDeleteDictionaryModal(val isOpen: Boolean): SettingsDictionariesAction
    object DeleteDictionary : SettingsDictionariesAction
    object EditDictionary : SettingsDictionariesAction
    object AddNewDictionary : SettingsDictionariesAction
    object ClearDictionarySelection : SettingsDictionariesAction
}