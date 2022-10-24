package com.ovolk.dictionary.presentation.settings_languages

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ovolk.dictionary.domain.model.select_languages.LanguagesType
import com.ovolk.dictionary.domain.use_case.settings_languages.GetSelectedLanguages
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SettingsLanguagesViewModel @Inject constructor(
    private val getSelectedLanguages: GetSelectedLanguages
) : ViewModel() {
    var state by mutableStateOf(SettingsLanguagesState())
        private set

    var listener: Listener? = null


    private fun getLanguages() {
        viewModelScope.launch {
            state = state.copy(
                languagesFrom = getSelectedLanguages.getLanguagesFrom(),
                languagesTo = getSelectedLanguages.getLanguagesTo()
            )
        }
    }

    fun onAction(action: SettingsLanguagesAction) {
        when (action) {
            is SettingsLanguagesAction.EditSelectedLanguages -> {
                listener?.navigate(action.type)
            }
            SettingsLanguagesAction.OnFocusScreen -> {
                getLanguages()
            }
        }
    }

    interface Listener {
        fun navigate(lang: LanguagesType)
    }
}