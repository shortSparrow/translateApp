package com.ovolk.dictionary.presentation.settings_exam_daily

data class SettingsExamDailyState(
    val countOfWords: String,
    val isStateChanges: Boolean = false,
    val isDoubleLanguageExamEnable: Boolean = false,
)

sealed interface SettingsExamDailyAction {
    data class ChangeCountOfWords(val count: String) : SettingsExamDailyAction
    object OnSaveChanges : SettingsExamDailyAction
    object OnToggleDoubleLanguageExam : SettingsExamDailyAction
}
