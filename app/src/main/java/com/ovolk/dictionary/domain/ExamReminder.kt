package com.ovolk.dictionary.domain

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.ComponentName
import android.content.SharedPreferences
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.ovolk.dictionary.data.workers.AlarmReceiver
import com.ovolk.dictionary.domain.model.exam_reminder.ReminderTime
import com.ovolk.dictionary.domain.use_case.exam.GetExamWordListUseCase
import com.ovolk.dictionary.domain.use_case.exam_remibder.GetTimeReminder
import com.ovolk.dictionary.util.*
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject


class ExamReminder @Inject constructor(
    private val application: Application,
    private val getExamWordListUseCase: GetExamWordListUseCase,
    private val getTimeReminder: GetTimeReminder
) {
    private val gson = Gson()

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
        val timeGson = gson.toJson(time)

        sharedPref.edit().apply {
            putInt(EXAM_REMINDER_FREQUENCY, frequency)
            putString(EXAM_REMINDER_TIME, timeGson)
            apply()
        }
        if (frequency != PushFrequency.NONE) {
            setReminder(timeInMillis = delay, interval = frequency.toLong())
        } else {
            resetReminder()
        }
    }

    private fun setReminder(timeInMillis: Long, interval: Long) {
        handleAlarmEnabling(PackageManager.COMPONENT_ENABLED_STATE_ENABLED)

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

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            timeInMillis,
            interval,
            pendingIntent
        )
    }

    private fun resetReminder() {
        handleAlarmEnabling(PackageManager.COMPONENT_ENABLED_STATE_DISABLED)

        sharedPref.edit().apply {
            putInt(EXAM_REMINDER_FREQUENCY, PushFrequency.NONE)
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

    private fun setInitialReminder() {
        var frequency = sharedPref.getInt(EXAM_REMINDER_FREQUENCY, PushFrequency.ONCE_AT_DAY)
        // if PushFrequency was reset when a user didn't have any word
        if (frequency == PushFrequency.NONE) {
            frequency = PushFrequency.ONCE_AT_DAY
        }

        val time = getTimeReminder(sharedPref)
        val delay =
            getExamReminderDelayFromNow(
                frequencyDelay = frequency,
                hours = time.hours,
                minutes = time.minutes
            )

        sharedPref.edit().apply {
                putInt(EXAM_REMINDER_FREQUENCY, frequency)
                putString(EXAM_REMINDER_TIME, gson.toJson(time))
            apply()
        }

        setReminder(timeInMillis = delay, interval = frequency.toLong());
    }

    /**
     * Notice that in the manifest, the boot receiver is set to android:enabled="false".
     * This means that the receiver will not be called unless the application explicitly enables it.
     * This prevents the boot receiver from being called unnecessarily.
     *
     * componentState can be PackageManager.COMPONENT_ENABLED_STATE_ENABLED or PackageManager.COMPONENT_ENABLED_STATE_DISABLED
     */
    private fun handleAlarmEnabling(componentState: Int) {
        val receiver = ComponentName(application.applicationContext, AlarmReceiver::class.java)
        application.applicationContext.packageManager.setComponentEnabledSetting(
            receiver,
            componentState,
            PackageManager.DONT_KILL_APP
        )
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
                        setInitialReminder()
                    }
                }
            }
        }
    }
}