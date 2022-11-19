package com.ovolk.dictionary.presentation.settings_languages

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.ovolk.dictionary.domain.model.select_languages.LanguagesType
import com.ovolk.dictionary.presentation.navigation.graph.CommonRotes
import com.ovolk.dictionary.presentation.settings_languages.components.SettingsLanguagePresenter


@Composable
fun SettingsLanguagesScreen(navController: NavHostController) {

    fun listener() = object : SettingsLanguagesViewModel.Listener {
        override fun navigate(lang: LanguagesType) {
            when (lang) {
                LanguagesType.LANG_TO -> {
                    navController.navigate("${CommonRotes.SETTINGS_LANGUAGES_TO}")
                }
                LanguagesType.LANG_FROM -> {
                    navController.navigate("${CommonRotes.SETTINGS_LANGUAGES_FROM}")
                }
            }
        }
    }


    val viewModel = hiltViewModel<SettingsLanguagesViewModel>()
    val state = viewModel.state
    if (viewModel.listener == null) {
        viewModel.listener = listener()
    }

    SettingsLanguagePresenter(state = state, onAction = viewModel::onAction)
}