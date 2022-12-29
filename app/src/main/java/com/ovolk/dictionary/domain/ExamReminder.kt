package com.ovolk.dictionary.domain

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.ovolk.dictionary.data.workers.AlarmReceiver
import com.ovolk.dictionary.domain.model.exam_reminder.ReminderTime
import com.ovolk.dictionary.domain.use_case.exam.GetExamWordListUseCase
import com.ovolk.dictionary.util.*
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject


class ExamReminder @Inject constructor(
    private val application: Application,
    private val getExamWordListUseCase: GetExamWordListUseCase
) {
    private val sharedPref: SharedPreferences = application.getSharedPreferences(
        SETTINGS_PREFERENCES,
        AppCompatActivity.MODE_PRIVATE
    )

    fun updateReminder(frequency: Int, startHour: Int, startMinute: Int) {
        val delay =
            getExamReminderDelayFromNow(
                frequencyDelay = frequency,
                hours = startHour,
                minutes = startMinute
            )
        val time = ReminderTime(hours = startHour, minutes = startMinute)
        val gson = Gson()
        val timeGson = gson.toJson(time)

        sharedPref.edit().apply {
            putInt(EXAM_REMINDER_FREQUENCY, frequency)
            putLong(TIME_TO_NEXT_REMINDER, delay)
            putString(EXAM_REMINDER_TIME, timeGson)
            apply()
        }
        if (frequency != PushFrequency.NONE) {
            setReminder(delay)
        } else {
            resetReminder()
        }
    }

    private fun setReminder(delay: Long) {
        val alarmManager =
            application.getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager
        val intent = AlarmReceiver.newIntent(application)

        val pendingIntent =
            PendingIntent.getBroadcast(
                application,
                EXAM_REMINDER_INTENT_CODE,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, delay, pendingIntent)
    }

    private fun resetReminder() {
        sharedPref.edit().apply {
            remove(TIME_TO_NEXT_REMINDER)
            apply()
        }

        val alarmManager =
            application.getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager
        val intent = AlarmReceiver.newIntent(application)
        val pendingIntent =
            PendingIntent.getBroadcast(
                application,
                EXAM_REMINDER_INTENT_CODE,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        pendingIntent.cancel()
        alarmManager.cancel(pendingIntent)
    }

    fun repeatReminder(isInitial: Boolean = false) {
        val gson = Gson()

        val frequency = sharedPref.getInt(EXAM_REMINDER_FREQUENCY, PushFrequency.ONCE_AT_DAY)
        if (frequency == PushFrequency.NONE) {
            return
        }
        val defaultValue = gson.toJson(
            ReminderTime(
                minutes = PushFrequency.DEFAULT_MINUTES,
                hours = PushFrequency.DEFAULT_HOURS
            )
        )
        val timePref = sharedPref.getString(EXAM_REMINDER_TIME, defaultValue)
        val time: ReminderTime = gson.fromJson(timePref, ReminderTime::class.java)

        val delay =
            getExamReminderDelayFromNow(
                frequencyDelay = frequency,
                hours = time.hours,
                minutes = time.minutes
            )

        sharedPref.edit().apply {
            putLong(TIME_TO_NEXT_REMINDER, delay)
            if (isInitial) {
                putInt(EXAM_REMINDER_FREQUENCY, frequency)
                putString(EXAM_REMINDER_TIME, gson.toJson(time))
            }
            apply()
        }

        setReminder(delay)
    }

    suspend fun setInitialReminderIfNeeded() {
        coroutineScope {
            val isWordListNoEmpty = getExamWordListUseCase.searchWordListSize()
            isWordListNoEmpty.collectLatest { count ->
                /**
                 * On first install wordCount = 0, if user add one word, we setup reminder.
                 * If user remove word and count = 0 we again reset reminder.
                 */
                when (count) {
                    0 -> resetReminder()
                    1 -> {
                        repeatReminder(isInitial = true)
                    }
                }
            }
        }
    }
}