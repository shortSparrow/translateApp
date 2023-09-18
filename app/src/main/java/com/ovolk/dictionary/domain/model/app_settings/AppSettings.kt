package com.ovolk.dictionary.domain.model.app_settings

import com.ovolk.dictionary.domain.model.exam_reminder.ReminderTime

data class AppSettings(
    val isWelcomeScreenPassed: Boolean,
    val showVariantsExamAvailableLanguages: List<String>,
    val reminder: ReminderSettings,
    val isDoubleLanguageExamEnable: Boolean,
    val examCountWords: String,
    val isExamAutoSuggestEnable: Boolean,
)

data class ReminderSettings(
    val examReminderFrequency: Int,
    val examReminderTime: ReminderTime,
)