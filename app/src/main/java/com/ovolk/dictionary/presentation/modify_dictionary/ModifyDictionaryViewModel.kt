package com.ovolk.dictionary.presentation.modify_dictionary

import android.app.Application
import androidx.compose.material.SnackbarDuration
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ovolk.dictionary.R
import com.ovolk.dictionary.domain.LoadingState
import com.ovolk.dictionary.domain.model.dictionary.Dictionary
import com.ovolk.dictionary.domain.model.modify_word.ValidateResult
import com.ovolk.dictionary.domain.model.select_languages.Language
import com.ovolk.dictionary.domain.model.select_languages.LanguagesType
import com.ovolk.dictionary.domain.response.Either
import com.ovolk.dictionary.domain.response.Failure
import com.ovolk.dictionary.domain.response.FailureWithCode
import com.ovolk.dictionary.domain.response.Success
import com.ovolk.dictionary.domain.snackbar.GlobalSnackbarManger
import com.ovolk.dictionary.domain.use_case.modify_dictionary.CrudDictionaryUseCase
import com.ovolk.dictionary.domain.use_case.modify_dictionary.DICTIONARY_ALREADY_EXIST
import com.ovolk.dictionary.domain.use_case.modify_dictionary.UNKNOWN_ERROR
import com.ovolk.dictionary.domain.use_case.modify_dictionary.util.ValidationFailure
import com.ovolk.dictionary.domain.use_case.select_languages.GetLanguageList
import com.ovolk.dictionary.presentation.core.snackbar.SnackBarError
import com.ovolk.dictionary.presentation.modify_dictionary.utils.generateDictionaryNameLangFrom
import com.ovolk.dictionary.presentation.modify_dictionary.utils.generateDictionaryNameLangTo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ModifyDictionaryViewModel @Inject constructor(
    private val dictionaryUseCase: CrudDictionaryUseCase,
    private val getLanguageList: GetLanguageList,
    private val savedStateHandle: SavedStateHandle,
    private val application: Application,
) : ViewModel() {
    var listener: Listener? = null
    var state by mutableStateOf(ModifyDictionaryState())
    private var _editedDictionary by mutableStateOf<Dictionary?>(null)

    init {
        state = state.copy(loadingState = LoadingState.PENDING)
        launchRightMode()
    }

    private fun getCurrentMode(): ModifyDictionaryModes {
        val mode = checkNotNull(savedStateHandle.get<String>("mode"))
        if (ModifyDictionaryModes.valueOf(mode) == ModifyDictionaryModes.MODE_EDIT) return ModifyDictionaryModes.MODE_EDIT
        return ModifyDictionaryModes.MODE_ADD
    }


    private fun launchRightMode() {
        val dictionaryId = checkNotNull(savedStateHandle.get<Long>("dictionaryId"))

        var languageFromList: List<Language>
        var languageToList: List<Language>

        if (getCurrentMode() == ModifyDictionaryModes.MODE_EDIT && dictionaryId != -1L) {
            viewModelScope.launch {
                val dictionary = dictionaryUseCase.getDictionary(dictionaryId = dictionaryId)

                when (dictionary) {
                    is Either.Failure -> {
                        state = state.copy(
                            loadingState = LoadingState.FAILED,
                            loadingError = dictionary.value.message,
                            screenTitle = application.getString(R.string.modify_dictionary_screen_title_mode_edit, "")
                        )
                    }

                    is Either.Success -> {
                        _editedDictionary = dictionary.value

                        languageFromList =
                            getLanguageList.getLanguageList(dictionary.value.langFromCode)
                        languageToList =
                            getLanguageList.getLanguageList(dictionary.value.langToCode)

                        state = state.copy(
                            dictionaryName = dictionary.value.title,
                            languageFromCode = dictionary.value.langFromCode,
                            languageToCode = dictionary.value.langToCode,
                            languageFromList = languageFromList,
                            languageToList = languageToList,
                            screenTitle = application.getString(R.string.modify_dictionary_screen_title_mode_edit, dictionary.value.title),
                            loadingState = LoadingState.SUCCESS,
                        )
                    }
                }
            }
        }

        if (getCurrentMode() == ModifyDictionaryModes.MODE_ADD) {
            state =
                state.copy(screenTitle = application.getString(R.string.modify_dictionary_screen_title_mode_add))

            languageFromList = getLanguageList.getLanguageList()
            languageToList = getLanguageList.getLanguageList()
            state = state.copy(
                languageFromList = languageFromList,
                languageToList = languageToList,
                loadingState = LoadingState.SUCCESS,
            )
        }
    }

    fun onAction(action: ModifyDictionaryAction) {
        when (action) {
            is ModifyDictionaryAction.OnSelectLanguage -> {
                when (state.languageBottomSheet.type) {
                    LanguagesType.LANG_FROM -> selectLanguageFrom(action.languageCode)
                    LanguagesType.LANG_TO -> selectLanguageTo(action.languageCode)
                    null -> {}
                }
            }

            is ModifyDictionaryAction.OnInputTitle -> {
                state = state.copy(
                    dictionaryName = action.value,
                    dictionaryNameValidation = ValidateResult(),
                )
            }

            ModifyDictionaryAction.SaveDictionary -> {
                viewModelScope.launch {
                    val response: Either<Success, Failure>
                    if (getCurrentMode() == ModifyDictionaryModes.MODE_EDIT && _editedDictionary != null) {
                        response = dictionaryUseCase.editDictionary(
                            id = _editedDictionary!!.id,
                            title = state.dictionaryName,
                            langToCode = state.languageToCode,
                            langFromCode = state.languageFromCode,
                            isActive = _editedDictionary!!.isActive
                        )

                    } else {
                        response = dictionaryUseCase.addDictionary(
                            title = state.dictionaryName,
                            langFromCode = state.languageFromCode,
                            langToCode = state.languageToCode,
                        )
                    }

                    accessOnSaveDictionary(response)
                }
            }

            is ModifyDictionaryAction.OpenLanguageBottomSheet -> {
                state = when (action.type) {
                    LanguagesType.LANG_TO -> {
                        state.copy(
                            languageBottomSheet = LanguageBottomSheet(
                                isOpen = true,
                                type = LanguagesType.LANG_TO,
                                languageList = state.languageToList,
                            )
                        )
                    }

                    LanguagesType.LANG_FROM -> {
                        state.copy(
                            languageBottomSheet = LanguageBottomSheet(
                                isOpen = true,
                                type = LanguagesType.LANG_FROM,
                                languageList = state.languageFromList
                            )
                        )
                    }
                }
            }

            ModifyDictionaryAction.CloseLanguageBottomSheet -> {
                state = state.copy(
                    languageBottomSheet = LanguageBottomSheet(
                        isOpen = false,
                        type = null
                    ),
                )
            }

            is ModifyDictionaryAction.OnSearchLanguage -> {
                val list = when (state.languageBottomSheet.type) {
                    LanguagesType.LANG_TO -> state.languageToList
                    LanguagesType.LANG_FROM -> state.languageFromList
                    null -> emptyList()
                }
                val filteredList = list.filter {
                    it.name.lowercase()
                        .contains(action.query.lowercase()) || it.nativeName.lowercase()
                        .contains(action.query.lowercase())
                }
                state =
                    state.copy(languageBottomSheet = state.languageBottomSheet.copy(languageList = filteredList))
            }

            is ModifyDictionaryAction.ToggleOpenDictionaryAlreadyExistModal -> {
                state = state.copy(dictionaryAlreadyExistModelOpen = action.isOpen)
            }
        }
    }

    private fun toggleLanguageList(list: List<Language>, languageCode: String): List<Language> {
        return list.map {
            if (it.langCode == languageCode) it.copy(
                isChecked = true
            ) else it.copy(isChecked = false)
        }
    }

    private fun selectLanguageFrom(languageCode: String) {
        val dictionaryName = generateDictionaryNameLangFrom(state.dictionaryName, languageCode)

        val languageFromList = toggleLanguageList(state.languageFromList, languageCode)

        state = state.copy(
            languageFromCode = languageCode,
            languageFromList = languageFromList,
            dictionaryName = dictionaryName,
            languageBottomSheet = state.languageBottomSheet.copy(languageList = languageFromList),
            langFromValidation = ValidateResult(),
            dictionaryNameValidation = ValidateResult(),
        )
    }

    private fun selectLanguageTo(languageCode: String) {
        val dictionaryName = generateDictionaryNameLangTo(state.dictionaryName, languageCode)
        val languageToList = toggleLanguageList(state.languageToList, languageCode)
        state = state.copy(
            languageToCode = languageCode,
            languageToList = languageToList,
            dictionaryName = dictionaryName,
            languageBottomSheet = state.languageBottomSheet.copy(languageList = languageToList),
            langToValidation = ValidateResult(),
            dictionaryNameValidation = ValidateResult(),

            )
    }

    private fun accessOnSaveDictionary(response: Either<Success, Failure>) {
        when (response) {
            is Either.Success -> {
                listener?.goBack()
            }

            is Either.Failure -> {
                if (response.value is ValidationFailure) {
                    state = state.copy(
                        dictionaryNameValidation = response.value.dictionaryName,
                        langToValidation = response.value.langTo,
                        langFromValidation = response.value.langFrom
                    )
                }

                if (response.value is FailureWithCode) {
                    when (response.value.code) {
                        DICTIONARY_ALREADY_EXIST -> {
                            state = state.copy(dictionaryAlreadyExistModelOpen = true)
                        }

                        UNKNOWN_ERROR -> {
                            GlobalSnackbarManger.showGlobalSnackbar(
                                duration = SnackbarDuration.Short,
                                data = SnackBarError(message = response.value.message),
                            )
                        }
                    }
                }

            }
        }
    }

    interface Listener {
        fun goBack()
    }
}