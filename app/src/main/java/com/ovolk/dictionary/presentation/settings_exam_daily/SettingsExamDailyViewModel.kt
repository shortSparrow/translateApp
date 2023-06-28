package com.ovolk.dictionary.presentation.settings_exam_daily

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ovolk.dictionary.R
import com.ovolk.dictionary.domain.repositories.AppSettingsRepository
import com.ovolk.dictionary.domain.response.Either
import com.ovolk.dictionary.domain.use_case.daily_exam_settings.HandleDailyExamSettingsUseCase
import com.ovolk.dictionary.domain.use_case.modify_dictionary.GetActiveDictionaryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsExamDailyViewModel @Inject constructor(
    private val handleDailyExamSettingsUseCase: HandleDailyExamSettingsUseCase,
    private val getActiveDictionaryUseCase: GetActiveDictionaryUseCase,
    private val application: Application,
    appSettingsRepository: AppSettingsRepository,
) : ViewModel() {
    val isDoubleLanguageExamEnable = appSettingsRepository.getAppSettings().isDoubleLanguageExamEnable
    var state by mutableStateOf(
        SettingsExamDailyState(
            countOfWords = handleDailyExamSettingsUseCase.getDailyExamSettings().countOfWords,
            isDoubleLanguageExamEnable = isDoubleLanguageExamEnable
        )
    )
    private var _initialState = SettingsExamDailyState(
        countOfWords = handleDailyExamSettingsUseCase.getDailyExamSettings().countOfWords,
        isDoubleLanguageExamEnable = isDoubleLanguageExamEnable,
    )

    init {
        viewModelScope.launch {
            when (val dictionary = getActiveDictionaryUseCase.getDictionaryActive()) {
                is Either.Failure -> {
                    state = state.copy(
                        languagesForDescription = listOf(
                            application.getString(R.string.settings_daily_exam_default_language_from),
                            application.getString(R.string.settings_daily_exam_default_language_to),
                        )
                    )
                }

                is Either.Success -> {
                    state = state.copy(
                        languagesForDescription = listOf(
                            dictionary.value.langFromCode,
                            dictionary.value.langToCode
                        )
                    )
                }
            }
        }
    }


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