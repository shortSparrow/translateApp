package com.ovolk.dictionary.presentation.settings

import android.os.Bundle
import android.view.View
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.fragment.findNavController
import com.google.accompanist.appcompattheme.AppCompatTheme
import com.ovolk.dictionary.databinding.FragmentSettingsBinding
import com.ovolk.dictionary.domain.model.settings.SettingsNavigation
import com.ovolk.dictionary.presentation.core.BaseFragment
import com.ovolk.dictionary.presentation.core.BindingInflater
import com.ovolk.dictionary.presentation.settings.components.SettingsList
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SettingsFragment : BaseFragment<FragmentSettingsBinding>() {
    override val bindingInflater: BindingInflater<FragmentSettingsBinding>
        get() = FragmentSettingsBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fragmentRoot.setContent {
            val viewModel = hiltViewModel<SettingsViewModel>()

            if (viewModel.listener == null) {
                viewModel.listener = listener()
            }
            val state = viewModel.state

            AppCompatTheme {
                SettingsList(list = state.settingsList, onAction = viewModel::onAction)
            }
        }
    }

    fun listener() = object : SettingsViewModel.Listener {
        override fun navigate(direction: SettingsNavigation) {
            when (direction) {
                SettingsNavigation.LANGUAGE_SETTINGS -> {
                    findNavController().navigate(SettingsFragmentDirections.actionSettingsFragmentToSettingsLanguagesFragment())
                }
                SettingsNavigation.EXAM_REMINDER_SETTINGS -> {
                    findNavController().navigate(SettingsFragmentDirections.actionSettingsFragmentToExamReminderFragment())
                }
            }
        }
    }
}