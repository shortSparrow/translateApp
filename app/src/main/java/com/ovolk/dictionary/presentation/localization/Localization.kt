package com.ovolk.dictionary.presentation.localization

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.ovolk.dictionary.presentation.localization.components.LocalizationPresenter

@Composable
fun LocalizationScreen(navController: NavHostController) {

    val viewModel = hiltViewModel<LocalizationViewModel>()
    val state = viewModel.state

    LaunchedEffect(Unit) {
        viewModel.listener = object : LocalizationViewModel.Listener {
            override fun goBack() {
                navController.popBackStack()
            }
        }
    }

    LocalizationPresenter(state = state, onAction = viewModel::onAction)
}