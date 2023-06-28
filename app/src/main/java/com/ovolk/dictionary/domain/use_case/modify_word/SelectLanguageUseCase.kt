//package com.ovolk.dictionary.domain.use_case.modify_word
//
//import com.ovolk.dictionary.domain.model.modify_word.ValidateResult
//import com.ovolk.dictionary.domain.model.select_languages.LanguagesType
//import com.ovolk.dictionary.presentation.modify_word.Languages
//import javax.inject.Inject
//
//class SelectLanguageUseCase @Inject constructor() {
//    private fun languageTo(state: Languages, langCode: String): Languages {
//        return state.copy(
//            languageToList = state.languageToList.map {
//                if (it.langCode == langCode) {
//                    return@map it.copy(isChecked = true)
//                }
//                return@map it.copy(isChecked = false)
//            },
//            selectLanguageToError = ValidateResult(true),
//        )
//    }
//
//    private fun languageFrom(state: Languages, langCode: String): Languages {
//        return state.copy(
//            languageFromList = state.languageFromList.map {
//                if (it.langCode == langCode) {
//                    return@map it.copy(isChecked = true)
//                }
//                return@map it.copy(isChecked = false)
//            },
//            selectLanguageFromError = ValidateResult(true),
//        )
//    }
//
//    fun selectLanguage(state: Languages, langCode: String, type: LanguagesType): Languages {
//        return when (type) {
//            LanguagesType.LANG_TO -> {
//                languageTo(state, langCode)
//            }
//            LanguagesType.LANG_FROM -> {
//                languageFrom(state, langCode)
//            }
//        }
//    }
//}