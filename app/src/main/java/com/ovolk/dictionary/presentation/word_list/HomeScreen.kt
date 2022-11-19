package com.ovolk.dictionary.presentation.word_list

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.ovolk.dictionary.presentation.modify_word.ModifyWordModes
import com.ovolk.dictionary.presentation.navigation.HomeRotes

@Composable
fun HomeScreen(navController: NavHostController) {

    fun listener() = object : WordListViewModel.Listener {
        override fun navigateToCreateNewWord() {
            navController.navigate( "${HomeRotes.MODIFY_WORD}/mode=${ModifyWordModes.MODE_ADD}")
        }

        override fun navigateToExistingWord(wordId: Long) {
            navController.navigate( "${HomeRotes.MODIFY_WORD}/mode=${ModifyWordModes.MODE_EDIT}?wordId=${wordId}")
        }
    }

    val viewModel = hiltViewModel<WordListViewModel>()
    val state = viewModel.state

    // prefilled search field and do search when open app from intent and pass searchWord
//    LaunchedEffect(args) {
//        args.searchedWord?.let {
//            viewModel.onAction(WordListAction.SearchWord(it))
//        }
//    }
    if (viewModel.listener == null) {
        viewModel.listener = listener()
    }

    WordList(state = state, onAction = viewModel::onAction)
}