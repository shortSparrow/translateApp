package com.ovolk.dictionary.presentation.settings

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.ovolk.dictionary.domain.model.settings.SettingsNavigation
import com.ovolk.dictionary.presentation.navigation.graph.CommonRotes
import com.ovolk.dictionary.presentation.settings.components.SettingsList


@Composable
fun SettingsScreen(navController: NavHostController) {

    fun listener() = object : SettingsViewModel.Listener {
        override fun navigate(direction: SettingsNavigation) {
            when (direction) {
                SettingsNavigation.LANGUAGE_SETTINGS -> {
                    navController.navigate("${CommonRotes.SETTINGS_LANGUAGES}")
                }
                SettingsNavigation.EXAM_REMINDER_SETTINGS -> {
                    navController.navigate("${CommonRotes.EXAM_REMINDER}")
                }
            }
        }
    }

    val viewModel = hiltViewModel<SettingsViewModel>()
    if (viewModel.listener == null) {
        viewModel.listener = listener()
    }
    val state = viewModel.state

    SettingsList(list = state.settingsList, onAction = viewModel::onAction)
}