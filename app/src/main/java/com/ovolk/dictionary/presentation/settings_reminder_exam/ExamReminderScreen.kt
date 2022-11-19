package com.ovolk.dictionary.presentation.settings_reminder_exam

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.ovolk.dictionary.presentation.settings_reminder_exam.components.ExamReminderPresenter

@Composable
fun ExamReminderScreen() {
    val viewModel = hiltViewModel<ExamReminderViewModel>()
    val state = viewModel.state

    ExamReminderPresenter(state, onAction = viewModel::onAction)
}