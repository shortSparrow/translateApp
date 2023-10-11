package com.ovolk.dictionary.domain.model.settings


data class SettingsItem(
    val title: String = "",
    val contentDescription: String = "",
    val iconId: Int,
    val navigateTo: SettingsNavigation
)

enum class SettingsNavigation { DICTIONARY_LIST, EXAM_REMINDER_SETTINGS, EXAM_DAILY_SETTINGS, LOCALIZATION }
