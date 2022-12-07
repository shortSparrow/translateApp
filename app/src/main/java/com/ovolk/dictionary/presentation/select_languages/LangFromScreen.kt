package com.ovolk.dictionary.presentation.select_languages

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.ovolk.dictionary.R
import com.ovolk.dictionary.domain.model.select_languages.LanguagesType
import com.ovolk.dictionary.presentation.navigation.graph.SelectLanguagesScreens
import com.ovolk.dictionary.presentation.select_languages.components.SelectLanguagesToFrom

@Composable
fun LangFromScreen(navController: NavHostController) {
    fun listener() = object : LanguagesToFromViewModel.ListenerLanguageFrom {
        override fun navigateToLanguageTo() {
            navController.navigate(SelectLanguagesScreens.LangTo.route)
        }
    }

    val viewModel = hiltViewModel<LanguagesToFromViewModel>()
    viewModel.setCurrentType(LanguagesType.LANG_FROM)

    val state = viewModel.state
    if (viewModel.listenerLanguageFrom == null) {
        viewModel.listenerLanguageFrom = listener()
    }
    SelectLanguagesToFrom(
        title = stringResource(id = R.string.select_languages_translate_language_from),
        state = state,
        onAction = viewModel::onAction,
        goBack = { navController.popBackStack() },
    )
}