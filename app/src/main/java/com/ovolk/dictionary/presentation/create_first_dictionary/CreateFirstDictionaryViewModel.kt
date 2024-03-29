package com.ovolk.dictionary.presentation.create_first_dictionary

import androidx.compose.material.SnackbarDuration
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ovolk.dictionary.domain.model.modify_word.ValidateResult
import com.ovolk.dictionary.domain.model.select_languages.Language
import com.ovolk.dictionary.domain.model.select_languages.LanguagesType
import com.ovolk.dictionary.domain.repositories.AppSettingsRepository
import com.ovolk.dictionary.domain.response.Either
import com.ovolk.dictionary.domain.response.Failure
import com.ovolk.dictionary.domain.response.FailureWithCode
import com.ovolk.dictionary.domain.response.Success
import com.ovolk.dictionary.domain.snackbar.GlobalSnackbarManger
import com.ovolk.dictionary.domain.use_case.modify_dictionary.CrudDictionaryUseCase
import com.ovolk.dictionary.domain.use_case.modify_dictionary.UNKNOWN_ERROR
import com.ovolk.dictionary.domain.use_case.modify_dictionary.util.ValidationFailure
import com.ovolk.dictionary.domain.use_case.select_languages.GetLanguageList
import com.ovolk.dictionary.domain.use_case.select_languages.GetPreferredLanguages
import com.ovolk.dictionary.presentation.core.snackbar.SnackBarError
import com.ovolk.dictionary.presentation.modify_dictionary.utils.generateDictionaryNameLangFrom
import com.ovolk.dictionary.presentation.modify_dictionary.utils.generateDictionaryNameLangTo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LocalState(
    val preferredLanguageListFrom: List<Language> = emptyList(),
    val preferredLanguageListTo: List<Language> = emptyList(),
    val languageToList: List<Language> = emptyList(),
    val languageFromList: List<Language> = emptyList(),
)

@HiltViewModel
class CreateFirstDictionaryViewModel @Inject constructor(
    private val dictionaryUseCase: CrudDictionaryUseCase,
    private val getLanguageList: GetLanguageList,
    private val getPreferredLanguages: GetPreferredLanguages,
    private val appSettingsRepository: AppSettingsRepository
) : ViewModel() {
    var listener: Listener? = null
    var state by mutableStateOf(FirstDictionaryState())
    private var _localState by mutableStateOf(LocalState())

    init {
        viewModelScope.launch {
            val languageFromList = getLanguageList.getLanguageList()
            val languageToList = getLanguageList.getLanguageList()

            val preferredLanguageListTo = getPreferredLanguages(languageToList)
            _localState = _localState.copy(
                preferredLanguageListTo = preferredLanguageListTo,
                languageFromList = languageFromList,
                languageToList = languageToList,
            )
        }
    }

    fun onAction(action: FirstDictionaryAction) {
        when (action) {
            is FirstDictionaryAction.OnSelectLanguage -> {
                when (state.languageBottomSheet.type) {
                    LanguagesType.LANG_FROM -> selectLanguageFrom(action.languageCode)
                    LanguagesType.LANG_TO -> selectLanguageTo(action.languageCode)
                    null -> {}
                }
            }

            is FirstDictionaryAction.OnInputTitle -> {
                state = state.copy(
                    dictionaryName = action.value,
                    dictionaryValidation = ValidateResult(),
                )
            }

            FirstDictionaryAction.SaveDictionary -> {
                viewModelScope.launch {
                    val response = dictionaryUseCase.addDictionary(
                        title = state.dictionaryName,
                        langFromCode = state.languageFromCode,
                        langToCode = state.languageToCode,
                    )

                    accessOnSaveDictionary(response)
                }
            }

            is FirstDictionaryAction.OpenLanguageBottomSheet -> {
                state = when (action.type) {
                    LanguagesType.LANG_TO -> {
                        state.copy(
                            languageBottomSheet = LanguageBottomSheet(
                                isOpen = true,
                                type = LanguagesType.LANG_TO,
                                filteredLanguageList = _localState.languageToList,
                                preferredLanguageList = _localState.preferredLanguageListTo
                            )
                        )
                    }

                    LanguagesType.LANG_FROM -> {
                        state.copy(
                            languageBottomSheet = LanguageBottomSheet(
                                isOpen = true,
                                type = LanguagesType.LANG_FROM,
                                filteredLanguageList = _localState.languageFromList,
                                preferredLanguageList = _localState.preferredLanguageListFrom,
                            )
                        )
                    }
                }
            }

            FirstDictionaryAction.CloseLanguageBottomSheet -> {
                state = state.copy(
                    languageBottomSheet = LanguageBottomSheet(
                        isOpen = false,
                        type = null
                    ),
                )
            }

            is FirstDictionaryAction.OnSearchLanguage -> {
                val list = when (state.languageBottomSheet.type) {
                    LanguagesType.LANG_TO -> _localState.languageToList
                    LanguagesType.LANG_FROM -> _localState.languageFromList
                    null -> emptyList()
                }
                val filteredList = list.filter {
                    it.name.lowercase().contains(action.query.lowercase()) ||
                            it.nativeName.lowercase().contains(action.query.lowercase())
                }
                state =
                    state.copy(languageBottomSheet = state.languageBottomSheet.copy(filteredLanguageList = filteredList))
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
        val languageFromList =
            toggleLanguageList(state.languageBottomSheet.filteredLanguageList, languageCode)

        _localState = _localState.copy(
            preferredLanguageListFrom = toggleLanguageList(
                _localState.preferredLanguageListFrom,
                languageCode
            )
        )

        state = state.copy(
            languageFromCode = languageCode,
//            languageFromList = languageFromList,
            dictionaryName = dictionaryName,
            languageBottomSheet = state.languageBottomSheet.copy(
                filteredLanguageList = languageFromList,
                preferredLanguageList = _localState.preferredLanguageListFrom
            ),
            langFromValidation = ValidateResult(),
            dictionaryValidation = ValidateResult(),
        )
    }

    private fun selectLanguageTo(languageCode: String) {
        val dictionaryName = generateDictionaryNameLangTo(state.dictionaryName, languageCode)
        val languageToList =
            toggleLanguageList(state.languageBottomSheet.filteredLanguageList, languageCode)
        _localState = _localState.copy(
            preferredLanguageListTo = toggleLanguageList(
                _localState.preferredLanguageListTo,
                languageCode
            )
        )

        state = state.copy(
            languageToCode = languageCode,
            dictionaryName = dictionaryName,
            languageBottomSheet = state.languageBottomSheet.copy(
                filteredLanguageList = languageToList,
                preferredLanguageList = _localState.preferredLanguageListTo
            ),
            langToValidation = ValidateResult(),
            dictionaryValidation = ValidateResult(),
        )
    }

    private fun accessOnSaveDictionary(response: Either<Success, Failure>) {
        when (response) {
            is Either.Success -> {
                appSettingsRepository.setAppSettings().apply {
                    isWelcomeScreenPassed(true)
                    update()
                }
                listener?.goToHome()
            }

            is Either.Failure -> {
                if (response.value is ValidationFailure) {
                    state = state.copy(
                        langFromValidation = response.value.langFrom,
                        langToValidation = response.value.langTo,
                        dictionaryValidation = response.value.dictionaryName
                    )
                }

                if (response.value is FailureWithCode) {
                    when (response.value.code) {
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
        fun goToHome()
    }
}