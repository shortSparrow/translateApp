package com.ovolk.dictionary.presentation.settings

import com.ovolk.dictionary.R
import com.ovolk.dictionary.domain.model.nearest_feature.NearestFeature
import com.ovolk.dictionary.domain.model.settings.SettingsItem
import com.ovolk.dictionary.domain.model.settings.SettingsNavigation
import com.ovolk.dictionary.presentation.DictionaryApp
import com.ovolk.dictionary.presentation.MainActivity

data class SettingsState(
    val settingsList: List<SettingsItem> = listOf(
        SettingsItem(
            title = MainActivity.getMainActivity()
                .getString(R.string.dictionary_list_screen_title),
            contentDescription = DictionaryApp.applicationContext()
                .getString(R.string.dictionary_list_screen_title_cd),
            iconId = R.drawable.language,
            navigateTo = SettingsNavigation.DICTIONARY_LIST
        ),
        SettingsItem(
            title = MainActivity.getMainActivity()
                .getString(R.string.settings_exam_reminder_item_title),
            contentDescription = DictionaryApp.applicationContext()
                .getString(R.string.settings_exam_reminder_cd_item_title),
            iconId = R.drawable.exam_reminder,
            navigateTo = SettingsNavigation.EXAM_REMINDER_SETTINGS
        ),
        SettingsItem(
            title = MainActivity.getMainActivity()
                .getString(R.string.settings_daily_exam_item_title),
            contentDescription = DictionaryApp.applicationContext()
                .getString(R.string.settings_daily_exam_cd_item_title),
            iconId = R.drawable.exam,
            navigateTo = SettingsNavigation.EXAM_DAILY_SETTINGS
        ),
        SettingsItem(
            title = MainActivity.getMainActivity()
                .getString(R.string.settings_languages_title),
            contentDescription = DictionaryApp.applicationContext()
                .getString(R.string.settings_languages_title_cd_item_title),
            iconId = R.drawable.localization,
            navigateTo = SettingsNavigation.LOCALIZATION
        ),
    ),
    val nearestFeatureList: List<NearestFeature> = emptyList()
)


sealed class SettingsAction {
    data class OnPressSettings(val settings: SettingsItem) : SettingsAction()
}