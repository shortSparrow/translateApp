package com.ovolk.dictionary.presentation.settings_languages

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavHostController
import com.ovolk.dictionary.domain.model.select_languages.LanguagesType
import com.ovolk.dictionary.presentation.navigation.graph.CommonRotes
import com.ovolk.dictionary.presentation.settings_languages.components.SettingsLanguagePresenter
import com.ovolk.dictionary.util.compose.OnLifecycleEvent


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

    // on focus load lang
    OnLifecycleEvent { _, event ->
        when (event) {
            Lifecycle.Event.ON_RESUME -> {
                viewModel.onAction(SettingsLanguagesAction.OnFocusScreen)
            }
            else -> {}
        }
    }

    SettingsLanguagePresenter(
        state = state,
        onAction = viewModel::onAction,
        onBack = { navController.popBackStack() }
    )
}