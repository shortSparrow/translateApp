package com.ovolk.dictionary.presentation.modify_dictionary

import com.ovolk.dictionary.domain.model.select_languages.Language
import com.ovolk.dictionary.domain.model.select_languages.LanguagesType

enum class ModifyDictionaryModes { MODE_ADD, MODE_EDIT }

data class ModifyDictionaryState(
    val isLoading: Boolean = true,
    val loadingError: String? = null,

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
    val languageList: List<Language> = emptyList()
)


sealed interface ModifyDictionaryAction {
    data class OnInputTitle(val value: String) : ModifyDictionaryAction
    data class OnSelectLanguage(val languageCode: String) :
        ModifyDictionaryAction
    data class OnSearchLanguage(val query:String): ModifyDictionaryAction
    object SaveDictionary : ModifyDictionaryAction
    data class OpenLanguageBottomSheet(val type: LanguagesType) : ModifyDictionaryAction
    object CloseLanguageBottomSheet : ModifyDictionaryAction
}