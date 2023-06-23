package com.ovolk.dictionary.presentation.list_full

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.ovolk.dictionary.presentation.list_full.components.ListFullPresenter
import com.ovolk.dictionary.presentation.modify_word.ModifyWordModes
import com.ovolk.dictionary.presentation.navigation.graph.MainTabRotes
import com.ovolk.dictionary.presentation.navigation.stack.CommonRotes


@Composable
fun ListFullScreen(navController: NavHostController, listId: Long, dictionaryId: Long) {

    fun listener() = object : ListsFullViewModel.Listener {
        override fun navigateToExam(listId: Long, listName: String, dictionaryId: Long?) {
            navController.navigate("${MainTabRotes.EXAM}?listName=${listName}&listId=${listId}&dictionaryId=${dictionaryId}")
        }

        override fun navigateToEditWord(wordId: Long) {
            navController.navigate("${CommonRotes.MODIFY_WORD}/mode=${ModifyWordModes.MODE_EDIT}?wordId=${wordId}")
        }

        override fun navigateToAddWord(listId: Long, dictionaryId: Long) {
            navController.navigate("${CommonRotes.MODIFY_WORD}/mode=${ModifyWordModes.MODE_ADD}?listId=${listId}&dictionaryId=${dictionaryId}")
        }
    }

    val viewModel = hiltViewModel<ListsFullViewModel>()
    if (viewModel.listener == null) {
        viewModel.listener = listener()
        viewModel.onAction(
            ListFullAction.InitialLoadData(
                listId = listId,
                dictionaryId = dictionaryId
            )
        )
    }
    val state = viewModel.state

    ListFullPresenter(
        state = state,
        onAction = viewModel::onAction,
        goBack = { navController.popBackStack() })

}