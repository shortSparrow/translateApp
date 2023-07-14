package com.ovolk.dictionary.presentation.word_list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.ovolk.dictionary.presentation.modify_word.ModifyWordModes
import com.ovolk.dictionary.presentation.navigation.graph.CommonRotes
import com.ovolk.dictionary.presentation.word_list.components.WordList

@Composable
fun HomeScreen(navController: NavHostController) {

    val viewModel = hiltViewModel<WordListViewModel>()
    val state = viewModel.state

    LaunchedEffect(Unit) {
        viewModel.listener = object : WordListViewModel.Listener {
            override fun navigateToCreateNewWord() {
                navController.navigate("${CommonRotes.MODIFY_WORD}/mode=${ModifyWordModes.MODE_ADD}")
            }

            override fun navigateToExistingWord(wordId: Long) {
                navController.navigate("${CommonRotes.MODIFY_WORD}/mode=${ModifyWordModes.MODE_EDIT}?wordId=${wordId}")
            }
        }
    }

    WordList(state = state, onAction = viewModel::onAction)
}