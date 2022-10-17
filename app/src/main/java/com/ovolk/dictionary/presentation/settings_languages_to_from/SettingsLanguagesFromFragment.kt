package com.ovolk.dictionary.presentation.settings_languages_to_from

import android.os.Bundle
import android.view.View
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.appcompattheme.AppCompatTheme
import com.ovolk.dictionary.databinding.FragmentSettingsLanguagesFromBinding
import com.ovolk.dictionary.domain.model.select_languages.LanguagesType
import com.ovolk.dictionary.presentation.core.BaseFragment
import com.ovolk.dictionary.presentation.core.BindingInflater
import com.ovolk.dictionary.presentation.settings_languages_to_from.components.SettingsLanguagesToScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsLanguagesFromFragment : BaseFragment<FragmentSettingsLanguagesFromBinding>() {

    override val bindingInflater: BindingInflater<FragmentSettingsLanguagesFromBinding>
        get() = FragmentSettingsLanguagesFromBinding::inflate


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fragmentRoot.setContent {
            val viewModel = hiltViewModel<SettingsLanguagesToFromViewModel>()
            viewModel.setCurrentType(LanguagesType.LANG_FROM)
            val state = viewModel.state

            AppCompatTheme {
                SettingsLanguagesToScreen(title = "select language from", state = state, onAction = viewModel::onAction)
            }
        }
    }

}