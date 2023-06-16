package com.ovolk.dictionary.presentation.create_first_dictionary

import com.ovolk.dictionary.domain.model.select_languages.Language
import com.ovolk.dictionary.domain.model.select_languages.LanguagesType


data class FirstDictionaryState(
    val dictionaryName: String = "",
    val dictionaryNameError: Boolean = false,

    val languageFromList: List<Language> = emptyList(),
    val languageFromCode: String? = null,
    val langFromError: Boolean = false,

    val languageToList: List<Language> = emptyList(),
    val languageToCode: String? = null,
    val langToError: Boolean = false,

    val langErrorMessage: String? = null,
    val languageBottomSheet: LanguageBottomSheet = LanguageBottomSheet(),
)

data class LanguageBottomSheet(
    val isOpen: Boolean = false,
    val type: LanguagesType? = null,
    val languageList: List<Language> = emptyList(),
    val preferredLanguageList: List<Language> = emptyList()
)


sealed interface FirstDictionaryAction {
    data class OnInputTitle(val value: String) : FirstDictionaryAction
    data class OnSelectLanguage(val languageCode: String) :
        FirstDictionaryAction
    data class OnSearchLanguage(val query:String): FirstDictionaryAction
    object SaveDictionary : FirstDictionaryAction
    data class OpenLanguageBottomSheet(val type: LanguagesType) : FirstDictionaryAction
    object CloseLanguageBottomSheet : FirstDictionaryAction
}