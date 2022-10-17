package com.ovolk.dictionary.presentation.settings_languages

import android.os.Bundle
import android.view.View
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.fragment.findNavController
import com.google.accompanist.appcompattheme.AppCompatTheme
import com.ovolk.dictionary.databinding.FragmentSettingsLanguagesBinding
import com.ovolk.dictionary.presentation.core.BaseFragment
import com.ovolk.dictionary.presentation.core.BindingInflater
import com.ovolk.dictionary.presentation.settings_languages.components.SettingsLanguageScreen
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SettingsLanguagesFragment : BaseFragment<FragmentSettingsLanguagesBinding>() {

    override val bindingInflater: BindingInflater<FragmentSettingsLanguagesBinding>
        get() = FragmentSettingsLanguagesBinding::inflate


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fragmentRoot.setContent {
            val viewModel = hiltViewModel<SettingsLanguagesViewModel>()
            val state = viewModel.state
            if (!viewModel.initialMount) {
                viewModel.setNavController(findNavController())
                viewModel.initialMount = true
            }

            AppCompatTheme {
                SettingsLanguageScreen(state = state, onAction = viewModel::onAction)
            }
        }
    }
}