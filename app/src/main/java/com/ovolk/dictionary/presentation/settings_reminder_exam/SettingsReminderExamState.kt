package com.ovolk.dictionary.presentation.settings_reminder_exam

import com.ovolk.dictionary.R
import com.ovolk.dictionary.domain.model.exam_reminder.FrequencyItem
import com.ovolk.dictionary.presentation.DictionaryApp
import com.ovolk.dictionary.util.PushFrequency


data class SettingsReminderExamState(
    val timeHours: Int = PushFrequency.DEFAULT_HOURS,
    val timeMinutes: Int = PushFrequency.DEFAULT_MINUTES,

    val reminderTime: String = "",
    val leftTimeToNextExam: String = "",
    val frequencyList: List<FrequencyItem> = listOf(
        FrequencyItem(title = readString(R.string.settings_exam_reminder_frequency_disable), PushFrequency.NONE),
        FrequencyItem(title = readString(R.string.settings_exam_reminder_frequency_once_a_day), PushFrequency.ONCE_AT_DAY),
        FrequencyItem(title = readString(R.string.settings_exam_reminder_frequency_once_every_3_days), PushFrequency.ONCE_AT_THREE_DAYS),
        FrequencyItem(title = readString(R.string.settings_exam_reminder_frequency_once_every_6_days), PushFrequency.ONCE_AT_SIX_DAYS),
    ),
    val selectedFrequency: FrequencyItem = FrequencyItem(
        title = DictionaryApp.applicationContext().getString(R.string.settings_exam_reminder_frequency_once_a_day),
        PushFrequency.ONCE_AT_DAY
    ),
    val isStateChanges: Boolean = false
) {
    companion object {
        fun readString(resId:Int):String {
            return DictionaryApp.applicationContext().getString(resId)
        }
    }

}

sealed interface OnExamReminderAction {
    data class OnChangeFrequency(val frequency: FrequencyItem): OnExamReminderAction
    data class OnChangeTime(val hours: Int, val minutes: Int): OnExamReminderAction
    object SaveChanges: OnExamReminderAction
}