package com.ovolk.dictionary.presentation.settings_languages_to_from

import android.os.Bundle
import android.view.View
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.appcompattheme.AppCompatTheme
import com.ovolk.dictionary.R
import com.ovolk.dictionary.databinding.FragmentSettingsLanguagesToBinding
import com.ovolk.dictionary.domain.model.select_languages.LanguagesType
import com.ovolk.dictionary.presentation.core.BaseFragment
import com.ovolk.dictionary.presentation.core.BindingInflater
import com.ovolk.dictionary.presentation.select_languages.components.Header
import com.ovolk.dictionary.presentation.settings_languages_to_from.components.SettingsLanguagesToScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsLanguagesToFragment : BaseFragment<FragmentSettingsLanguagesToBinding>() {

    override val bindingInflater: BindingInflater<FragmentSettingsLanguagesToBinding>
        get() = FragmentSettingsLanguagesToBinding::inflate


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fragmentRoot.setContent {
            val viewModel = hiltViewModel<SettingsLanguagesToFromViewModel>()
            viewModel.setCurrentType(LanguagesType.LANG_TO)
            val state = viewModel.state

            AppCompatTheme {
                Column {
                    Header(
                        title = stringResource(id = R.string.settings_languages_to_title),
                        wiBackButton = true
                    )
                    SettingsLanguagesToScreen(
                        state = state,
                        onAction = viewModel::onAction
                    )
                }
            }
        }
    }
}