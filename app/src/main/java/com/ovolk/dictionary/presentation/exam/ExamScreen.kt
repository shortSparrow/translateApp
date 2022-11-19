package com.ovolk.dictionary.presentation.exam

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.ovolk.dictionary.presentation.modify_word.ModifyWordModes
import com.ovolk.dictionary.presentation.navigation.HomeRotes

@Composable
fun ExamScreen(navController: NavHostController, listName: String, listId: Long) {


    val viewModel = hiltViewModel<ExamKnowledgeWordsViewModel>()
    val state = viewModel.composeState
    val onAction = viewModel::onAction


//    // TODO temporary solution for updating exam list after create first word
    if (state.examWordList.isEmpty() && state.shouldLoadWordListAgain) {
        onAction(ExamAction.LoadExamList(listId = listId, listName = listName))
    }

    LaunchedEffect(listName,listId) {
        onAction(ExamAction.LoadExamList(listId = listId, listName = listName))
    }

    if (viewModel.listener == null) {
        viewModel.listener = object : ExamKnowledgeWordsViewModel.Listener {
            override fun onNavigateToCreateFirstWord() {
                navController.navigate("${HomeRotes.MODIFY_WORD}/mode=${ModifyWordModes.MODE_ADD}")
            }
        }
    }

    ExamPresenter(state = state, onAction = onAction)
}