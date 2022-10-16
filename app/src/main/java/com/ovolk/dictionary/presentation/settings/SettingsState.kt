package com.ovolk.dictionary.presentation.settings

import com.ovolk.dictionary.R
import com.ovolk.dictionary.domain.model.settings.SettingsItem
import com.ovolk.dictionary.domain.model.settings.SettingsNavigation
import com.ovolk.dictionary.presentation.DictionaryApp

data class SettingsState(
    val settingsList: List<SettingsItem> = listOf(
        SettingsItem(
            title = DictionaryApp.applicationContext()
                .getString(R.string.settings_language_item_title),
            contentDescription = DictionaryApp.applicationContext()
                .getString(R.string.settings_language_cd_item_title),
            iconId = R.drawable.language,
            navigateTo = SettingsNavigation.LANGUAGE_SETTINGS
        ),
        SettingsItem(
            title = DictionaryApp.applicationContext()
                .getString(R.string.settings_exam_reminder_item_title),
            contentDescription = DictionaryApp.applicationContext()
                .getString(R.string.settings_exam_reminder_cd_item_title),
            iconId = R.drawable.exam_reminder,
            navigateTo = SettingsNavigation.EXAM_REMINDER_SETTINGS
        ),
    )
)


sealed class SettingsAction {
    data class OnPressSettings(val settings: SettingsItem) : SettingsAction()
}