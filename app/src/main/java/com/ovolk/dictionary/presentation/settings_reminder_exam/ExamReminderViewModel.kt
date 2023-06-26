package com.ovolk.dictionary.presentation.settings_reminder_exam

import android.app.Application
import android.os.CountDownTimer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.ovolk.dictionary.domain.ExamReminder
import com.ovolk.dictionary.domain.repositories.AppSettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class ExamReminderViewModel @Inject constructor(
    val application: Application,
    private val examReminder: ExamReminder,
    appSettingsRepository: AppSettingsRepository
) : ViewModel() {
    var state by mutableStateOf(SettingsReminderExamState())
        private set

    private var initialValues = SettingsReminderExamState()
    var timer: CountDownTimer? = null


    init {
        val reminderSettings = appSettingsRepository.getAppSettings().reminder
        val frequency = reminderSettings.examReminderFrequency
        val time = reminderSettings.examReminderTime
        val hours = convertTime(time.hours)
        val minutes = convertTime(time.minutes)

        state = state.copy(
            timeHours = time.hours,
            timeMinutes = time.minutes,
            selectedFrequency = state.frequencyList.find { it.value == frequency }!!,
            reminderTime = "${hours}:${minutes}"
        )
        initialValues = state
    }

    fun onAction(action: OnExamReminderAction) {
        when (action) {
            is OnExamReminderAction.OnChangeFrequency -> {
                state = state.copy(selectedFrequency = action.frequency)
                checkIsChangedExist()
            }

            OnExamReminderAction.SaveChanges -> {
                updateSettingsPreferences()
            }

            is OnExamReminderAction.OnChangeTime -> {
                val hours = convertTime(action.hours)
                val minutes = convertTime(action.minutes)

                state = state.copy(
                    timeHours = action.hours,
                    timeMinutes = action.minutes,
                    reminderTime = "${hours}:${minutes}"
                )
                checkIsChangedExist()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        timer?.cancel()
    }

    private fun updateSettingsPreferences() {
        try {
            examReminder.updateReminder(
                frequency = state.selectedFrequency.value,
                startHour = state.timeHours,
                startMinute = state.timeMinutes
            )

            initialValues = state
            checkIsChangedExist()
        } catch (e: Exception) {
            Timber.e("Error update reminder $e")
        }
    }


    private fun checkIsChangedExist() {
        val isSame =
            state.selectedFrequency != initialValues.selectedFrequency ||
                    state.timeHours != initialValues.timeHours ||
                    state.timeMinutes != initialValues.timeMinutes

        state = state.copy(isStateChanges = isSame)
    }

    private fun convertTime(value: Int): String {
        return if (value < 10) "0$value" else value.toString()
    }
}