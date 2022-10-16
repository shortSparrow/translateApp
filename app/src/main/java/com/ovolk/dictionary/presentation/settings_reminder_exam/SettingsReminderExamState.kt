package com.ovolk.dictionary.presentation.settings_reminder_exam

import com.ovolk.dictionary.domain.model.exam_reminder.FrequencyItem
import com.ovolk.dictionary.util.PushFrequency


data class SettingsReminderExamState(
    val timeHours: Int = PushFrequency.DEFAULT_HOURS,
    val timeMinutes: Int = PushFrequency.DEFAULT_MINUTES,

    val reminderTime: String = "",
    val leftTimeToNextExam: String = "",
    val frequencyList: List<FrequencyItem> = listOf(
        FrequencyItem(title = "disable", PushFrequency.NONE),
        FrequencyItem(title = "once a day", PushFrequency.ONCE_AT_DAY),
        FrequencyItem(title = "once every 3 days", PushFrequency.ONCE_AT_THREE_DAYS),
        FrequencyItem(title = "once every 6 days", PushFrequency.ONCE_AT_SIX_DAYS),
    ),
    val selectedFrequency: FrequencyItem = FrequencyItem(
        title = "once a day",
        PushFrequency.ONCE_AT_DAY
    ),
    val isStateChanges: Boolean = false
)

sealed class OnExamReminderAction() {
    data class OnChangeFrequency(val frequency: FrequencyItem): OnExamReminderAction()
    data class OnChangeTime(val hours: Int, val minutes: Int): OnExamReminderAction()
    object SaveChanges: OnExamReminderAction()
}