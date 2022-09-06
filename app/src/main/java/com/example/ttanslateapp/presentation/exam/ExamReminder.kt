package com.example.ttanslateapp.presentation.exam

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import com.example.ttanslateapp.data.workers.AlarmReceiver
import com.example.ttanslateapp.domain.use_case.exam.GetExamWordListUseCase
import com.example.ttanslateapp.util.*
import com.google.gson.Gson
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber
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

        Timber.d("DELAY: ${delay}")
        sharedPref.edit().apply {
            putInt(EXAM_REMINDER_FREQUENCY, frequency)
            putLong(LEFT_BEFORE_NOTIFICATION, delay)
            putString(EXAM_REMINDER_TIME, timeGson)
            commit()
        }

        setReminder(delay)
    }

    private fun setReminder(delay: Long) {
        val alarmManager =
            application.getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager
        val intent = AlarmReceiver.newIntent(application)

        val pendingIntent =
            PendingIntent.getBroadcast(application, 100, intent, PendingIntent.FLAG_IMMUTABLE)
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, delay, pendingIntent)
    }


    fun resetReminder() {
        sharedPref.edit().apply {
            remove(LEFT_BEFORE_NOTIFICATION)
            commit()
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
            commit()
        }

        setReminder(delay)
    }

    suspend fun setInitialReminderIfNeeded() {
        coroutineScope {
            val isWordListNoEmpty = getExamWordListUseCase.searchWordListSize()
            isWordListNoEmpty.collectLatest { count ->
                when(count) {
                    0 -> resetReminder()
                    1 -> repeatReminder()
                }
            }
        }
    }
}