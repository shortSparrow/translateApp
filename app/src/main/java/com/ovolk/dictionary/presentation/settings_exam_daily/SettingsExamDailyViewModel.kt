package com.ovolk.dictionary.presentation.settings_exam_daily

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.ovolk.dictionary.domain.use_case.daily_exam_settings.HandleDailyExamSettingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.handleCoroutineException
import javax.inject.Inject

@HiltViewModel
class SettingsExamDailyViewModel @Inject constructor(
    private val handleDailyExamSettingsUseCase: HandleDailyExamSettingsUseCase
) : ViewModel() {

    var state by mutableStateOf(
        SettingsExamDailyState(
            countOfWords = handleDailyExamSettingsUseCase.getDailyExamSettings().countOfWords
        )
    )
    private var _initialState = SettingsExamDailyState(
        countOfWords = handleDailyExamSettingsUseCase.getDailyExamSettings().countOfWords
    )

    fun onAction(action: SettingsExamDailyAction) {
        when (action) {
            is SettingsExamDailyAction.ChangeCountOfWords -> {
                val isStateChanges = _initialState.countOfWords != action.count
                state = state.copy(countOfWords = action.count, isStateChanges = isStateChanges)
            }
            SettingsExamDailyAction.OnSaveChanges -> {
                handleDailyExamSettingsUseCase.saveDailyExamSettings(state.countOfWords)
                resetInitialState()
            }
        }
    }

    private fun resetInitialState() {
        _initialState = state
        state = state.copy(isStateChanges = false)
    }
}