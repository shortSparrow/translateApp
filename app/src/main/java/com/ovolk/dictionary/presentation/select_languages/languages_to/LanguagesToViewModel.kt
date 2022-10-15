package com.ovolk.dictionary.presentation.select_languages.languages_to

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.google.gson.Gson
import com.ovolk.dictionary.domain.use_case.select_languages.*
import com.ovolk.dictionary.presentation.select_languages.LanguagesToActions
import com.ovolk.dictionary.presentation.select_languages.LanguageState
import com.ovolk.dictionary.util.LANGUAGE_TO
import com.ovolk.dictionary.util.USER_STATE_PREFERENCES
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class LanguagesToViewModel @Inject constructor(
    val application: Application,
    getPreferredLanguages: GetPreferredLanguages,
    getLanguageList: GetLanguageList,
    val searchLanguageList: SearchLanguageList,
    val updateLanguageList: UpdateLanguageList,
    val updateTranslatableLanguages: UpdateTranslatableLanguages
) : ViewModel() {
    var state by mutableStateOf(LanguageState())
        private set

    var initialMount = false
    private var navController: NavController? = null

    fun setNavController(navController: NavController) {
        this.navController = navController
    }

    init {
        val langList = getLanguageList.getLanguageListTo()
        val preferredLanguage = getPreferredLanguages(langList)
        state = state.copy(
            languageList = langList,
            filteredLanguageList = langList,
            preferredLanguages = preferredLanguage
        )
    }

    fun onAction(actions: LanguagesToActions) {
        when (actions) {
            LanguagesToActions.NavigateToLanguagesToFrom -> {
                updateTranslatableLanguages.saveLanguagesTo(list = state.languageList)
                navController?.navigate(LanguagesToFragmentDirections.actionLanguageToFragmentToLanguageFromFragment())
            }
            is LanguagesToActions.OnSearchLanguagesTo -> {
                state = state.copy(
                    filteredLanguageList = searchLanguageList(
                        languageList = state.languageList,
                        query = actions.query
                    )
                )
            }
            is LanguagesToActions.ToggleCheck -> {
                val newFilteredState = updateLanguageList(
                    list = state.filteredLanguageList,
                    toggleLanguage = actions.language
                )

                val newLanguageList = updateLanguageList(
                    list = state.languageList,
                    toggleLanguage = actions.language
                )

                val newPreferredLanguage = updateLanguageList(
                    list = state.preferredLanguages,
                    toggleLanguage = actions.language
                )

                state = state.copy(
                    filteredLanguageList = newFilteredState,
                    languageList = newLanguageList,
                    preferredLanguages = newPreferredLanguage
                )
            }
        }
    }
}