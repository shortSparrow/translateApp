package com.ovolk.dictionary.presentation.list_full

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.appcompattheme.AppCompatTheme
import com.ovolk.dictionary.presentation.modify_word.ModifyWordModes
import com.ovolk.dictionary.presentation.navigation.graph.HomeRotes
import com.ovolk.dictionary.presentation.navigation.graph.MainTabRotes


@Composable
fun ListFullScreen(navController: NavHostController, listId: Long, listName: String) {

    fun listener() = object : ListsFullViewModel.Listener {
        override fun goBack() {
            navController.popBackStack()
        }

        override fun navigateToExam(listId: Long, listName: String) {
            navController.navigate("${MainTabRotes.EXAM}?listName=${listName}&listId=${listId}")
        }

        override fun navigateToEditWord(wordId: Long) {
            navController.navigate("${HomeRotes.MODIFY_WORD}/mode=${ModifyWordModes.MODE_EDIT}?wordId=${wordId}")
        }

        override fun navigateToAddWord(listId: Long) {
            // here
//            navController.navigate("${HomeRotes.MODIFY_WORD}/mode=${ModifyWordModes.MODE_ADD}?listId=${listId}")
            navController.navigate("${HomeRotes.MODIFY_WORD}/mode=${ModifyWordModes.MODE_ADD}?listId=${listId}")
        }
    }

    val viewModel = hiltViewModel<ListsFullViewModel>()
    if (viewModel.listener == null) {
        viewModel.listener = listener()
        viewModel.onAction(ListFullAction.InitialLoadData(listId = listId, listName = listName))
    }

    val state = viewModel.state

    AppCompatTheme {
        ListFullPresenter(state = state, onAction = viewModel::onAction)
    }


}