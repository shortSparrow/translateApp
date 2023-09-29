package com.ovolk.dictionary.presentation.localization

import androidx.compose.material.SnackbarDuration
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.ovolk.dictionary.domain.repositories.AppSettingsRepository
import com.ovolk.dictionary.domain.snackbar.GlobalSnackbarManger
import com.ovolk.dictionary.domain.use_case.localization.SetAppLanguageUseCase
import com.ovolk.dictionary.domain.use_case.select_languages.GetLanguageList
import com.ovolk.dictionary.presentation.DictionaryApp
import com.ovolk.dictionary.presentation.core.snackbar.SnackBarError
import com.ovolk.dictionary.util.appLanguages
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class LocalizationViewModel @Inject constructor(
    val appSettingsRepository: AppSettingsRepository,
    getLanguageList: GetLanguageList,
    private val setAppLanguageUseCase: SetAppLanguageUseCase
) : ViewModel() {
    var listener: Listener? = null
    private var _state by mutableStateOf(LocalizationStatePrivate())
    var state by mutableStateOf(LocalizationState())
        private set

    init {
        val appLanguageCode = appSettingsRepository.getAppSettings().appLanguageCode.lowercase()
        val langList =
            getLanguageList.getIntersectionLanguageList(
                selectedLangCode = appLanguageCode,
                intersectionLanguageCodes = appLanguages
            )

        _state = _state.copy(languageList = langList)
        state =
            state.copy(
                selectedLanguage = _state.languageList.first { it.isChecked },
                languageFilteredList = _state.languageList,
                isLoading = false
            )
    }

    fun onAction(action: LocalizationAction) {
        when (action) {
            LocalizationAction.OnPressGoBack -> {
                listener?.goBack()
            }

            is LocalizationAction.OnSearchLanguage -> {
                state = state.copy(
                    languageFilteredList = _state.languageList.filter {
                        it.name.lowercase()
                            .contains(action.language.lowercase()) || it.nativeName.lowercase()
                            .contains(action.language.lowercase())
                    }
                )
            }

            is LocalizationAction.OnConfirmChangeAppLanguage -> {
                action.languageCode?.let { languageCode ->
                    SetAppLanguageUseCase().setLocale(
                        DictionaryApp.applicationContext(),
                        languageCode
                    )

                    appSettingsRepository.setAppSettings().apply {
                        appLanguage(languageCode)
                        updateSynchronously()
                    }

//                    val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(languageCode)
//                    AppCompatDelegate.setApplicationLocales(appLocale)
                    setAppLanguageUseCase.relaunchApp(DictionaryApp.applicationContext())

                    val newAppLanguage =
                        _state.languageList.find { it.langCode == languageCode }
                    _state = _state.copy(languageList = _state.languageList.map {
                        if (it.langCode == languageCode) {
                            return@map it.copy(isChecked = true)
                        }
                        return@map it.copy(isChecked = false)
                    })
                    state = state.copy(
                        selectedLanguage = newAppLanguage,
                        languageFilteredList = _state.languageList,
                        isConfirmAppChangeLanguageModalOpen = false,
                        isConfirmAppChangeLanguage = null
                    )


                } ?: run {
                    GlobalSnackbarManger.showGlobalSnackbar(
                        duration = SnackbarDuration.Short,
                        data = SnackBarError(message = "NO langauge"),
                    )
                }


            }

            LocalizationAction.OnCloseConfirmChangeAppLanguageModal -> {
                state = state.copy(
                    isConfirmAppChangeLanguageModalOpen = false,
                    isConfirmAppChangeLanguage = null
                )
            }

            is LocalizationAction.OnOpenConfirmChangeAppLanguageModal -> {
                if (state.selectedLanguage?.langCode != action.language?.langCode) {
                    state = state.copy(
                        isConfirmAppChangeLanguageModalOpen = true,
                        isConfirmAppChangeLanguage = action.language
                    )
                }
            }
        }
    }

    interface Listener {
        fun goBack()
    }
}