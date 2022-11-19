package com.ovolk.dictionary.presentation.settings_languages_to_from

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.ovolk.dictionary.R
import com.ovolk.dictionary.domain.model.select_languages.LanguagesType
import com.ovolk.dictionary.presentation.core.compose.header.Header
import com.ovolk.dictionary.presentation.settings_languages_to_from.components.SettingsLanguagesToFromPresenter

@Composable
fun SettingsLanguagesToScreen() {
    val viewModel = hiltViewModel<SettingsLanguagesToFromViewModel>()
    viewModel.setCurrentType(LanguagesType.LANG_TO)
    val state = viewModel.state

    Column {
        Header(
            title = stringResource(id = R.string.settings_languages_to_title),
            withBackButton = true
        )
        SettingsLanguagesToFromPresenter(
            state = state,
            onAction = viewModel::onAction
        )
    }

}