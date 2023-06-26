package com.ovolk.dictionary.presentation.settings_dictionaries

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.ovolk.dictionary.presentation.modify_dictionary.ModifyDictionaryModes
import com.ovolk.dictionary.presentation.navigation.stack.CommonRotes
import com.ovolk.dictionary.presentation.settings_dictionaries.components.SettingsDictionariesPresenter


@Composable
fun DictionaryListScreen(navController: NavHostController) {

    fun listener() = object : DictionaryListViewModel.Listener {
        override fun goToModifyDictionary(dictionaryId: Long?) {
            val mode =
                if (dictionaryId == null) ModifyDictionaryModes.MODE_ADD else ModifyDictionaryModes.MODE_EDIT
            navController.navigate("${CommonRotes.MODIFY_DICTIONARY}/mode=${mode}?dictionaryId=${dictionaryId}")
        }

        override fun goToDictionaryWords(dictionaryId: Long) {
            navController.navigate("${CommonRotes.DICTIONARY_WORDS}?dictionaryId=${dictionaryId}")
        }
    }

    val viewModel = hiltViewModel<DictionaryListViewModel>()
    val state = viewModel.state
    if (viewModel.listener == null) {
        viewModel.listener = listener()
    }

    SettingsDictionariesPresenter(
        state = state,
        onAction = viewModel::onAction,
        onBack = { navController.popBackStack() }
    )
}