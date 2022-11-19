package com.ovolk.dictionary.presentation.settings_reminder_exam

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.ovolk.dictionary.presentation.settings_reminder_exam.components.ExamReminderPresenter

@Composable
fun ExamReminderScreen(navController: NavHostController) {
    val viewModel = hiltViewModel<ExamReminderViewModel>()
    val state = viewModel.state

    ExamReminderPresenter(
        state,
        onAction = viewModel::onAction,
        goBack = { navController.popBackStack() })
}