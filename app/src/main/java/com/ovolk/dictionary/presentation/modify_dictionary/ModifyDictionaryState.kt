package com.ovolk.dictionary.presentation.modify_dictionary

import com.ovolk.dictionary.domain.LoadingState
import com.ovolk.dictionary.domain.model.modify_word.ValidateResult
import com.ovolk.dictionary.domain.model.select_languages.Language
import com.ovolk.dictionary.domain.model.select_languages.LanguagesType

enum class ModifyDictionaryModes { MODE_ADD, MODE_EDIT }

data class ModifyDictionaryState(
    val loadingState: LoadingState = LoadingState.IDLE,
    val loadingError: String? = null,
    val screenTitle: String = "",

    val dictionaryName: String = "",
    val dictionaryNameValidation: ValidateResult = ValidateResult(),

    val languageFromList: List<Language> = emptyList(),
    val languageFromCode: String? = null,
    val langFromValidation: ValidateResult = ValidateResult(),

    val languageToList: List<Language> = emptyList(),
    val languageToCode: String? = null,
    val langToValidation: ValidateResult = ValidateResult(),

    val languageBottomSheet: LanguageBottomSheet = LanguageBottomSheet(),
    val dictionaryAlreadyExistModelOpen: Boolean = false
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
    data class ToggleOpenDictionaryAlreadyExistModal(val isOpen: Boolean) : ModifyDictionaryAction
}