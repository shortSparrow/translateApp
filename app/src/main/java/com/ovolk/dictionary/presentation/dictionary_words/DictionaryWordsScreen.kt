package com.ovolk.dictionary.presentation.dictionary_words

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.ovolk.dictionary.presentation.dictionary_words.components.DictionaryWords
import com.ovolk.dictionary.presentation.modify_dictionary.ModifyDictionaryModes
import com.ovolk.dictionary.presentation.modify_word.ModifyWordModes
import com.ovolk.dictionary.presentation.navigation.graph.MainTabRotes
import com.ovolk.dictionary.presentation.navigation.graph.CommonRotes

@Composable
fun DictionaryWordsScreen(navController: NavHostController) {
    val viewModel = hiltViewModel<DictionaryWordsViewModel>()
    val state = viewModel.state

    LaunchedEffect(Unit) {
        viewModel.listener = object : DictionaryWordsViewModel.Listener {
            override fun goToEditDictionary(dictionaryId: Long) {
                navController.navigate("${CommonRotes.MODIFY_DICTIONARY}/mode=${ModifyDictionaryModes.MODE_EDIT}?dictionaryId=${dictionaryId}")
            }

            override fun goBack() {
                navController.popBackStack()
            }

            override fun goToAddNewWord(dictionaryId: Long) {
                navController.navigate("${CommonRotes.MODIFY_WORD}/mode=${ModifyWordModes.MODE_ADD}?dictionaryId=${dictionaryId}")
            }

            override fun goToEditWord(wordId: Long) {
                navController.navigate("${CommonRotes.MODIFY_WORD}/mode=${ModifyWordModes.MODE_EDIT}?wordId=${wordId}")
            }

            override fun goToExam(dictionaryId: Long) {
                navController.navigate("${MainTabRotes.EXAM}?dictionaryId=${dictionaryId}")
            }
        }

    }

    DictionaryWords(state = state, onAction = viewModel::onAction, goBack={navController.popBackStack()})
}