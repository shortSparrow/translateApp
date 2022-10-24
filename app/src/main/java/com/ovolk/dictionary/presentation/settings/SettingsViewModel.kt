package com.ovolk.dictionary.presentation.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.ovolk.dictionary.domain.model.settings.SettingsNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor() : ViewModel() {
    var state by mutableStateOf(SettingsState())
        private set

    var listener: Listener? = null

    fun onAction(action: SettingsAction) {
        when (action) {
            is SettingsAction.OnPressSettings -> {
                listener?.navigate(action.settings.navigateTo)
            }
        }
    }

    interface Listener {
        fun navigate(direction: SettingsNavigation)
    }

}