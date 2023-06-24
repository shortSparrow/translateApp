package com.ovolk.dictionary.presentation.settings_exam_daily

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.ovolk.dictionary.presentation.settings_exam_daily.components.SettingsExamDailyPresenter

@Composable
fun SettingsExamDailyScreen(
    navController: NavHostController,
) {
    val viewModel = hiltViewModel<SettingsExamDailyViewModel>()
    val state = viewModel.state
    val onAction = viewModel::onAction

    SettingsExamDailyPresenter(navController = navController, state = state, onAction = onAction)
}

