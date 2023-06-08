package com.ovolk.dictionary.presentation.settings_exam_daily

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.ovolk.dictionary.data.in_memory_storage.ExamLocalCache
import com.ovolk.dictionary.domain.use_case.daily_exam_settings.HandleDailyExamSettingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsExamDailyViewModel @Inject constructor(
    private val handleDailyExamSettingsUseCase: HandleDailyExamSettingsUseCase
) : ViewModel() {
    private val examLocalCache = ExamLocalCache.getInstance()
    var state by mutableStateOf(
        SettingsExamDailyState(
            countOfWords = handleDailyExamSettingsUseCase.getDailyExamSettings().countOfWords,
            isDoubleLanguageExamEnable = examLocalCache.getIsDoubleLanguageExamEnable()
        )
    )
    private var _initialState = SettingsExamDailyState(
        countOfWords = handleDailyExamSettingsUseCase.getDailyExamSettings().countOfWords,
        isDoubleLanguageExamEnable = examLocalCache.getIsDoubleLanguageExamEnable(),
    )


    fun onAction(action: SettingsExamDailyAction) {
        when (action) {
            is SettingsExamDailyAction.ChangeCountOfWords -> {
                state = state.copy(countOfWords = action.count)
                state = state.copy(isStateChanges = stateHasChanged())
            }

            SettingsExamDailyAction.OnSaveChanges -> {
                handleDailyExamSettingsUseCase.saveDailyExamSettings(
                    state.countOfWords,
                    state.isDoubleLanguageExamEnable
                )
                resetInitialState()
            }

            SettingsExamDailyAction.OnToggleDoubleLanguageExam -> {
                state = state.copy(isDoubleLanguageExamEnable = !state.isDoubleLanguageExamEnable)
                state = state.copy(isStateChanges = stateHasChanged())
            }
        }
    }

    private fun stateHasChanged(): Boolean {
        return state.countOfWords != _initialState.countOfWords || state.isDoubleLanguageExamEnable != _initialState.isDoubleLanguageExamEnable
    }

    private fun resetInitialState() {
        _initialState = state
        state = state.copy(isStateChanges = false)
    }
}