package com.ovolk.dictionary.presentation.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.ovolk.dictionary.domain.model.settings.SettingsNavigation
import com.ovolk.dictionary.presentation.navigation.graph.CommonRotes
import com.ovolk.dictionary.presentation.settings.components.SettingsList


@Composable
fun SettingsScreen(navController: NavHostController) {

    val viewModel = hiltViewModel<SettingsViewModel>()

    LaunchedEffect(Unit) {
        viewModel.listener = object : SettingsViewModel.Listener {
            override fun navigate(direction: SettingsNavigation) {
                when (direction) {
                    SettingsNavigation.DICTIONARY_LIST -> {
                        navController.navigate("${CommonRotes.DICTIONARY_LIST}")
                    }

                    SettingsNavigation.EXAM_REMINDER_SETTINGS -> {
                        navController.navigate("${CommonRotes.EXAM_REMINDER}")
                    }

                    SettingsNavigation.EXAM_DAILY_SETTINGS -> {
                        navController.navigate("${CommonRotes.EXAM_DAILY}")
                    }
                }
            }
        }
    }

    val state = viewModel.state

    SettingsList(list = state.settingsList, onAction = viewModel::onAction)
}