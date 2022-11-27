package com.ovolk.dictionary.presentation.select_languages

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.ovolk.dictionary.domain.model.select_languages.LanguagesType
import com.ovolk.dictionary.domain.use_case.select_languages.*
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class LanguagesToFromViewModel @Inject constructor(
    val application: Application,
    val getPreferredLanguages: GetPreferredLanguages,
    private val getLanguageList: GetLanguageList,
    val searchLanguageList: SearchLanguageList,
    val updateLanguageList: UpdateLanguageList,
    private val updateTranslatableLanguages: UpdateTranslatableLanguages
) : ViewModel() {
    var state by mutableStateOf(LanguageToFromState())
        private set

    var listenerLanguageFrom: ListenerLanguageFrom? = null
    var listenerLanguageTo: ListenerLanguageTo? = null

    fun setCurrentType(type: LanguagesType) {
        if (state.type == null) {
            val langList = getLanguageList.getLanguageListByKey(type)
            val preferredLanguage = getPreferredLanguages(langList)

            state = state.copy(
                languageList = langList,
                filteredLanguageList = langList,
                preferredLanguages = preferredLanguage,
                type = type,
                headerWithBackButton = when (type) {
                    LanguagesType.LANG_TO -> true
                    LanguagesType.LANG_FROM -> false
                }
            )
        }
    }

    fun onAction(action: LanguagesToFromActions) {
        when (action) {
            LanguagesToFromActions.GoNext -> {
                when (state.type) {
                    LanguagesType.LANG_TO -> {
                        updateTranslatableLanguages.saveLanguagesTo(list = state.languageList)
                        listenerLanguageTo?.navigateToHome()
                    }
                    LanguagesType.LANG_FROM -> {
                        updateTranslatableLanguages.saveLanguagesFrom(list = state.languageList)
                        listenerLanguageFrom?.navigateToLanguageTo()
                    }
                    null -> {}
                }
            }
            is LanguagesToFromActions.OnSearchLanguagesTo -> {
                state = state.copy(
                    filteredLanguageList = searchLanguageList(
                        languageList = state.languageList,
                        query = action.query
                    )
                )
            }
            is LanguagesToFromActions.ToggleCheck -> {
                val newFilteredState = updateLanguageList(
                    list = state.filteredLanguageList,
                    toggledLanguage = action.language
                )

                val newLanguageList = updateLanguageList(
                    list = state.languageList,
                    toggledLanguage = action.language
                )

                val newPreferredLanguage = updateLanguageList(
                    list = state.preferredLanguages,
                    toggledLanguage = action.language
                )

                state = state.copy(
                    filteredLanguageList = newFilteredState,
                    languageList = newLanguageList,
                    preferredLanguages = newPreferredLanguage
                )
            }
        }
    }

    interface ListenerLanguageFrom {
        fun navigateToLanguageTo()
    }

    interface ListenerLanguageTo {
        fun navigateToHome()
    }
}