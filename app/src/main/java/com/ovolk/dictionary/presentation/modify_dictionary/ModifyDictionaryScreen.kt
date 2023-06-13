package com.ovolk.dictionary.presentation.modify_dictionary

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.ovolk.dictionary.presentation.modify_dictionary.components.ModifyDictionaryPresenter

@Composable
fun ModifyDictionaryScreen(navController: NavHostController) {
    val viewModel = hiltViewModel<ModifyDictionaryViewModel>()
    val state = viewModel.state

    // TODO add modal if there are unsaved changes
    LaunchedEffect(Unit) {
        viewModel.listener = object : ModifyDictionaryViewModel.Listener {
            override fun goBack() {
                navController.popBackStack()
            }
        }
    }

    ModifyDictionaryPresenter(
        state = state,
        onAction = viewModel::onAction,
        goBack = { viewModel.listener?.goBack() },
    )
}