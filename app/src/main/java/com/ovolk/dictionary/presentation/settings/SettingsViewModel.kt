package com.ovolk.dictionary.presentation.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ovolk.dictionary.domain.model.settings.SettingsNavigation
import com.ovolk.dictionary.domain.repositories.NearestFeatureRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val nearestFeatureRepository: NearestFeatureRepository
) : ViewModel() {
    var state by mutableStateOf(SettingsState())
        private set

    var listener: Listener? = null

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val list = nearestFeatureRepository.getNearestFeature()
            withContext(Dispatchers.Main) {
                state = state.copy(nearestFeatureList = list)
            }
        }
    }

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