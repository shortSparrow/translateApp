package com.ovolk.dictionary.domain.use_case.exam_remibder

import com.google.gson.Gson
import com.ovolk.dictionary.domain.model.exam_reminder.ReminderTime
import com.ovolk.dictionary.util.PushFrequency
import javax.inject.Inject

// TODO maybe convert to mapper
class GetTimeReminder @Inject constructor() {
    operator fun invoke(jsonExamReminderTime:String): ReminderTime {
        return gson.fromJson(jsonExamReminderTime, ReminderTime::class.java)
    }

    companion object {
        val gson = Gson()
        val defaultValue: String = gson.toJson(
            ReminderTime(
                minutes = PushFrequency.DEFAULT_MINUTES,
                hours = PushFrequency.DEFAULT_HOURS
            )
        )
    }
}