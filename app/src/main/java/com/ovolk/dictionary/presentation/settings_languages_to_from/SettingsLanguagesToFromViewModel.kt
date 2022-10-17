package com.ovolk.dictionary.presentation.settings_languages_to_from

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.ovolk.dictionary.domain.model.select_languages.LanguagesType
import com.ovolk.dictionary.domain.use_case.select_languages.GetLanguageList
import com.ovolk.dictionary.domain.use_case.select_languages.SearchLanguageList
import com.ovolk.dictionary.domain.use_case.select_languages.UpdateLanguageList
import com.ovolk.dictionary.domain.use_case.select_languages.UpdateTranslatableLanguages
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
open class SettingsLanguagesToFromViewModel @Inject constructor(
    val updateLanguageList: UpdateLanguageList,
    val searchLanguageList: SearchLanguageList,
    private val getLanguageList: GetLanguageList,
    private val updateTranslatableLanguages: UpdateTranslatableLanguages
) : ViewModel() {
    var state by mutableStateOf(SettingsLanguagesToFromState())
        private set

    private var currentType: LanguagesType? = null

    fun setCurrentType(type:LanguagesType) {
        if (currentType == null) {
            currentType = type

            val langList = getLanguageList.getLanguageListByKey(type)

            state = state.copy(
                languageList = langList,
                filteredLanguageList = langList,
                selectedLanguages = langList.filter { it.isChecked }
            )
        }
    }


    fun onAction(action: SettingsLanguagesToFromActions) {
        when (action) {
            is SettingsLanguagesToFromActions.OnSearchLanguagesToFrom -> {
                state = state.copy(
                    filteredLanguageList = searchLanguageList(
                        languageList = state.languageList,
                        query = action.query
                    )
                )
            }

            is SettingsLanguagesToFromActions.ToggleCheck -> {
                val newFilteredState = updateLanguageList(
                    list = state.filteredLanguageList,
                    toggledLanguage = action.language
                )

                val newLanguageList = updateLanguageList(
                    list = state.languageList,
                    toggledLanguage = action.language
                )

                val newSelectedLanguages = newLanguageList.filter { it.isChecked }

                state = state.copy(
                    filteredLanguageList = newFilteredState,
                    languageList = newLanguageList,
                    selectedLanguages = newSelectedLanguages
                )
                currentType?.let {
                    updateTranslatableLanguages.saveLanguagesByKey(newSelectedLanguages, it)
                }
            }
        }
    }
}