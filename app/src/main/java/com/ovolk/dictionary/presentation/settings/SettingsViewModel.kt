package com.ovolk.dictionary.presentation.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.ovolk.dictionary.domain.model.settings.SettingsNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor() : ViewModel() {
    var state by mutableStateOf(SettingsState())
        private set

    var initialMount = false
    private var navController: NavController? = null

    fun setNavController(navController: NavController) {
        this.navController = navController
        initialMount = true
    }


    fun onAction(action: SettingsAction) {
        when (action) {
            is SettingsAction.OnPressSettings -> {
                when (action.settings.navigateTo) {
                    SettingsNavigation.LANGUAGE_SETTINGS -> {
                        navController?.navigate(SettingsFragmentDirections.actionSettingsFragmentToSettingsLanguagesFragment())
                    }
                    SettingsNavigation.EXAM_REMINDER_SETTINGS -> {
                        navController?.navigate(SettingsFragmentDirections.actionSettingsFragmentToExamReminderFragment())
                    }
                }
            }
        }
    }
}