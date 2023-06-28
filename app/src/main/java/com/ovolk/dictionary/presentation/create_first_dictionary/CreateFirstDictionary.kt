package com.ovolk.dictionary.presentation.create_first_dictionary

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.ovolk.dictionary.presentation.create_first_dictionary.components.CreateFirstDictionaryScreen
import com.ovolk.dictionary.presentation.modify_dictionary.components.SelectLanguageBottomSheet
import com.ovolk.dictionary.presentation.navigation.graph.Graph

@Composable
fun CreateFirstDictionary(navController: NavHostController) {
    val viewModel = hiltViewModel<CreateFirstDictionaryViewModel>()
    val state = viewModel.state
    val onAction = viewModel::onAction



    LaunchedEffect(Unit) {
        viewModel.listener = object : CreateFirstDictionaryViewModel.Listener {
            override fun goToHome() {
                navController.navigate(Graph.MAIN_TAB_BAR)
            }
        }
    }

    SelectLanguageBottomSheet(
        isBottomSheetOpen = state.languageBottomSheet.isOpen,
        languageList = state.languageBottomSheet.filteredLanguageList,
        onSelectLanguage = { langCode ->
            onAction(FirstDictionaryAction.OnSelectLanguage(languageCode = langCode))
        },
        onSearchLanguage = { value -> onAction(FirstDictionaryAction.OnSearchLanguage(value)) },
        closeLanguageBottomSheet = { onAction(FirstDictionaryAction.CloseLanguageBottomSheet) },
        preferredLanguages = state.languageBottomSheet.preferredLanguageList,
        body = {
            CreateFirstDictionaryScreen(state = state, onAction = onAction)
        },
    )
}

