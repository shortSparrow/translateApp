package com.ovolk.dictionary.presentation.settings_languages

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
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

    var initialMount = false
    private var navController: NavController? = null

    fun setNavController(navController: NavController) {
        this.navController = navController
    }


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
                when (action.type) {
                    LanguagesType.LANG_TO -> {
                        navController?.navigate(SettingsLanguagesFragmentDirections.actionSettingsLanguagesFragmentToSettingsLanguagesToFragment())
                    }
                    LanguagesType.LANG_FROM -> {
                        navController?.navigate(SettingsLanguagesFragmentDirections.actionSettingsLanguagesFragmentToSettingsLanguagesFromFragment())
                    }
                }
            }
            SettingsLanguagesAction.OnFocusScreen -> {
                getLanguages()
            }
        }
    }

}