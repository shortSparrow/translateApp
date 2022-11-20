package com.ovolk.dictionary.presentation.settings_reminder_exam

import android.app.Application
import android.content.SharedPreferences
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ovolk.dictionary.domain.use_case.exam_remibder.GetTimeReminder
import com.ovolk.dictionary.domain.ExamReminder
import com.ovolk.dictionary.util.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject


@HiltViewModel
class ExamReminderViewModel @Inject constructor(
    val application: Application,
    private val examReminder: ExamReminder,
    getTimeReminder: GetTimeReminder
) : ViewModel() {
    var state by mutableStateOf(SettingsReminderExamState())
        private set

    private var initialValues = SettingsReminderExamState()
    var timer: CountDownTimer? = null

    private val sharedPref: SharedPreferences =
        application.getSharedPreferences(
            MY_PREFERENCES,
            AppCompatActivity.MODE_PRIVATE
        )

    init {
        val frequency = sharedPref.getInt(EXAM_REMINDER_FREQUENCY, PushFrequency.ONCE_AT_DAY)

        val time = getTimeReminder(sharedPref)
        val hours = convertTime(time.hours)
        val minutes = convertTime(time.minutes)


        state = state.copy(
            timeHours = time.hours,
            timeMinutes = time.minutes,
            selectedFrequency = state.frequencyList.find { it.value == frequency }!!,
            reminderTime = "${hours}:${minutes}"
        )
        initialValues = state
        showTimeBeforePush()
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

        }
        showTimeBeforePush()
    }

    private fun showTimeBeforePush() {
        timer?.cancel()

        val getNotificationPref =
            sharedPref.getLong(TIME_TO_NEXT_REMINDER, -1L)
        if (getNotificationPref == -1L) {
            viewModelScope.launch {
                // delay is needed for prevent override two uiState, which goe one by one
//                delay(500) // TODO maybe delete
                state = state.copy(leftTimeToNextExam = "")
            }
            return
        }

        val millis = getNotificationPref - Calendar.getInstance().timeInMillis

        // FIXME maybe replace it to main activity
//        if (millis < 0) {
//            if (sharedPref.getInt(
//                    EXAM_REMINDER_FREQUENCY,
//                    PushFrequency.ONCE_AT_DAY
//                ) != PushFrequency.NONE
//            ) {
//                Toast.makeText(application, "restart PUSH notification", Toast.LENGTH_LONG).show()
//            }
//        }

        if (millis > 0) {
            timer = object : CountDownTimer(millis, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    val hms = convertTimeToHMS(millisUntilFinished)
                    state = state.copy(leftTimeToNextExam = hms)
                }

                override fun onFinish() {
                    // repeat timer
                    listenReminderRepeat()
                }
            }
            timer?.start()
        }
    }

    private fun listenReminderRepeat() = viewModelScope.launch {
        var listen = true
        while (listen) {
            val isReady = sharedPref.getLong(TIME_TO_NEXT_REMINDER, -1L)
            if (isReady !== -1L) {
                listen = false
            }
            delay(500)
        }
        showTimeBeforePush()
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