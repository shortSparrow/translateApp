package com.ovolk.dictionary.presentation.select_languages

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.ovolk.dictionary.R
import com.ovolk.dictionary.domain.model.select_languages.LanguagesType
import com.ovolk.dictionary.presentation.navigation.Graph
import com.ovolk.dictionary.presentation.select_languages.components.SelectLanguagesToFrom


@Composable
fun LanguagesToScreen(navController: NavHostController) {
    fun listener() = object : LanguagesToFromViewModel.ListenerLanguageTo {
        override fun navigateToHome() {
            navController.popBackStack()
            navController.navigate(Graph.MAIN_TAB_BAR)
        }
    }

    val viewModel = hiltViewModel<LanguagesToFromViewModel>()
    viewModel.setCurrentType(LanguagesType.LANG_TO)
    if (viewModel.listenerLanguageTo == null) {
        viewModel.listenerLanguageTo = listener()
    }
    val state = viewModel.state

    SelectLanguagesToFrom(
        title = stringResource(id = R.string.select_languages_translate_language_to),
        state = state,
        onAction = viewModel::onAction,
        navController=navController,
    )
}