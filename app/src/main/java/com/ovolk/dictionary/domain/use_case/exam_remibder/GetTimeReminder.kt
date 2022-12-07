package com.ovolk.dictionary.domain.use_case.exam_remibder

import android.content.SharedPreferences
import com.google.gson.Gson
import com.ovolk.dictionary.domain.model.exam_reminder.ReminderTime
import com.ovolk.dictionary.util.EXAM_REMINDER_TIME
import com.ovolk.dictionary.util.PushFrequency
import javax.inject.Inject

class GetTimeReminder @Inject constructor() {
    operator fun invoke(sharedPref: SharedPreferences): ReminderTime {
        val gson = Gson()
        val defaultValue = gson.toJson(
            ReminderTime(
                minutes = PushFrequency.DEFAULT_MINUTES,
                hours = PushFrequency.DEFAULT_HOURS
            )
        )
        val examReminderTime = sharedPref.getString(EXAM_REMINDER_TIME, defaultValue).toString()

        return gson.fromJson(examReminderTime, ReminderTime::class.java)
    }

}