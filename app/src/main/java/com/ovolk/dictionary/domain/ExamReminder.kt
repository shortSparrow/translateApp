package com.ovolk.dictionary.domain

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.ComponentName
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.SnackbarDuration
import com.google.gson.Gson
import com.ovolk.dictionary.R
import com.ovolk.dictionary.data.workers.AlarmReceiver
import com.ovolk.dictionary.domain.model.exam_reminder.ReminderTime
import com.ovolk.dictionary.domain.repositories.AppSettingsRepository
import com.ovolk.dictionary.domain.response.Either
import com.ovolk.dictionary.domain.snackbar.GlobalSnackbarManger
import com.ovolk.dictionary.domain.use_case.exam.GetExamWordListUseCase
import com.ovolk.dictionary.domain.use_case.modify_dictionary.GetActiveDictionaryUseCase
import com.ovolk.dictionary.presentation.DictionaryApp
import com.ovolk.dictionary.presentation.core.snackbar.SnackBarAlert
import com.ovolk.dictionary.util.EXAM_REMINDER_INTENT_CODE
import com.ovolk.dictionary.util.PushFrequency
import com.ovolk.dictionary.util.getExamReminderDelayFromNow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


class ExamReminder @Inject constructor(
    private val application: Application,
    private val getExamWordListUseCase: GetExamWordListUseCase,
    private val appSettingsRepository: AppSettingsRepository,
    private val getActiveDictionaryUseCase: GetActiveDictionaryUseCase
) {
    private val gson = Gson()
    private val scope = CoroutineScope(Dispatchers.IO);

    fun getIsAlarmExist(): Boolean {
        val intent = AlarmReceiver.newIntent(application)

        return PendingIntent.getBroadcast(
            DictionaryApp.applicationContext(),
            EXAM_REMINDER_INTENT_CODE,
            intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        ) != null
    }

    fun updateReminder(frequency: Int, startHour: Int, startMinute: Int) {
        val delay =
            getExamReminderDelayFromNow(
                frequencyDelay = frequency,
                hours = startHour,
                minutes = startMinute
            )
        val time = ReminderTime(hours = startHour, minutes = startMinute)
        val timeGson = gson.toJson(time)

        appSettingsRepository.setAppSettings().apply {
            examReminderFrequency(frequency)
            examReminderTime(timeGson)
            update()
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

        appSettingsRepository.setAppSettings().apply {
            examReminderFrequency(PushFrequency.NONE)
            update()
        }

        cancelReminder()
    }

    fun cancelReminder() {
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

     fun setInitialReminder() {
        val reminderSettings = appSettingsRepository.getAppSettings().reminder
        var frequency = reminderSettings.examReminderFrequency
        // if PushFrequency was reset when a user didn't have any word
        if (frequency == PushFrequency.NONE) {
            frequency = PushFrequency.ONCE_AT_DAY
        }

        val time = reminderSettings.examReminderTime
        val delay =
            getExamReminderDelayFromNow(
                frequencyDelay = frequency,
                hours = time.hours,
                minutes = time.minutes
            )
        appSettingsRepository.setAppSettings().apply {
            examReminderFrequency(frequency)
            examReminderTime(gson.toJson(time))
            update()
        }

        setReminder(timeInMillis = delay, interval = frequency.toLong())
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
        scope.launch {
            val activeDictionary = getActiveDictionaryUseCase.getDictionaryActive()
            if (activeDictionary is Either.Success) {
                val isWordListNoEmpty =
                    getExamWordListUseCase.searchWordListSize(activeDictionary.value.id)
                isWordListNoEmpty.collectLatest { count ->
                    /**
                     * On first install wordCount = 0, if user add one word, we setup reminder.
                     * If user remove word and count = 0 we again reset reminder.
                     */
                    when (count) {
                        0 -> resetReminder()
                        1 -> {
                            if (!getIsAlarmExist()) {
                                setInitialReminder()
                            }
                        }

                        else -> {
                            if (!getIsAlarmExist()) {
                                GlobalSnackbarManger.showGlobalSnackbar(
                                    duration = SnackbarDuration.Long,
                                    data = SnackBarAlert(message = application.getString(R.string.exam_reminder_receiver_alarm_was_canceled))
                                )

                                setInitialReminder()
                            }
                        }
                    }
                }
            }
        }
    }
}