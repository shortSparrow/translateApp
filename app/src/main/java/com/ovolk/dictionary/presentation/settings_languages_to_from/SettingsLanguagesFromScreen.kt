package com.ovolk.dictionary.presentation.settings_languages_to_from

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.ovolk.dictionary.R
import com.ovolk.dictionary.domain.model.select_languages.LanguagesType
import com.ovolk.dictionary.presentation.core.compose.header.Header
import com.ovolk.dictionary.presentation.settings_languages_to_from.components.SettingsLanguagesToFromPresenter

@Composable
fun SettingsLanguagesFromScreen(navController: NavHostController) {
    val viewModel = hiltViewModel<SettingsLanguagesToFromViewModel>()
    viewModel.setCurrentType(LanguagesType.LANG_FROM)
    val state = viewModel.state

    Column {
        Header(
            title = stringResource(id = R.string.settings_languages_from_title),
            withBackButton = true,
            onBackButtonClick = { navController.popBackStack() }
        )
        SettingsLanguagesToFromPresenter(state = state, onAction = viewModel::onAction)
    }
}