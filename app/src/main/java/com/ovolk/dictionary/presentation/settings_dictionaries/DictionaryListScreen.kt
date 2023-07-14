package com.ovolk.dictionary.presentation.settings_dictionaries

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.ovolk.dictionary.presentation.modify_dictionary.ModifyDictionaryModes
import com.ovolk.dictionary.presentation.navigation.graph.CommonRotes
import com.ovolk.dictionary.presentation.settings_dictionaries.components.SettingsDictionariesPresenter


@Composable
fun DictionaryListScreen(navController: NavHostController) {

    val viewModel = hiltViewModel<DictionaryListViewModel>()
    val state = viewModel.state

    LaunchedEffect(Unit) {
        viewModel.listener = object : DictionaryListViewModel.Listener {
            override fun goToModifyDictionary(dictionaryId: Long?) {
                val mode =
                    if (dictionaryId == null) ModifyDictionaryModes.MODE_ADD else ModifyDictionaryModes.MODE_EDIT
                navController.navigate("${CommonRotes.MODIFY_DICTIONARY}/mode=${mode}?dictionaryId=${dictionaryId}")
            }

            override fun goToDictionaryWords(dictionaryId: Long) {
                navController.navigate("${CommonRotes.DICTIONARY_WORDS}?dictionaryId=${dictionaryId}")
            }
        }
    }


    SettingsDictionariesPresenter(
        state = state,
        onAction = viewModel::onAction,
        onBack = { navController.popBackStack() }
    )
}