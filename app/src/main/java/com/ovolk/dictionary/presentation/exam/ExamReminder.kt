package com.ovolk.dictionary.presentation.exam

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import com.ovolk.dictionary.data.workers.AlarmReceiver
import com.ovolk.dictionary.domain.use_case.exam.GetExamWordListUseCase
import com.ovolk.dictionary.util.*
import com.google.gson.Gson
import com.ovolk.dictionary.presentation.settings.SettingsUiState
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber
import java.util.*
import javax.inject.Inject

data class ReminderTime(
    val hours: Int,
    val minutes: Int,
)


class ExamReminder @Inject constructor(
    private val application: Application,
    private val getExamWordListUseCase: GetExamWordListUseCase
) {
    private val sharedPref: SharedPreferences = application.getSharedPreferences(
        MY_PREFERENCES,
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


        if (frequency == PushFrequency.NONE) {
            resetReminder().apply {
                sharedPref.edit().apply {
                    putString(EXAM_REMINDER_TIME, timeGson)
                    putInt(EXAM_REMINDER_FREQUENCY, frequency)
                    apply()
                }
            }
        } else {
            sharedPref.edit().apply {
                putInt(EXAM_REMINDER_FREQUENCY, frequency)
                putLong(LEFT_BEFORE_NOTIFICATION, delay)
                putString(EXAM_REMINDER_TIME, timeGson)
                apply()
            }

            setReminder(delay)
        }
    }

    private fun setReminder(delay: Long) {
        val alarmManager =
            application.getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager
        val intent = AlarmReceiver.newIntent(application)

        val pendingIntent =
            PendingIntent.getBroadcast(application, 100, intent, PendingIntent.FLAG_IMMUTABLE)
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, delay, pendingIntent)
    }


    private fun resetReminder() {
        sharedPref.edit().apply {
            remove(LEFT_BEFORE_NOTIFICATION)
            apply()
        }

        val alarmManager =
            application.getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager
        val intent = AlarmReceiver.newIntent(application)
        val pendingIntent =
            PendingIntent.getBroadcast(application, 100, intent, PendingIntent.FLAG_IMMUTABLE)
        alarmManager.cancel(pendingIntent)
    }

    fun repeatReminder() {
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
            putLong(LEFT_BEFORE_NOTIFICATION, delay)
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
                    1 -> repeatReminder()
                }
            }
        }
    }
}