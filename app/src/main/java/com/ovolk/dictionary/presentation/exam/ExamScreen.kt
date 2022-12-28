package com.ovolk.dictionary.presentation.exam

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.ovolk.dictionary.presentation.exam.components.ExamPresenter
import com.ovolk.dictionary.presentation.modify_word.ModifyWordModes
import com.ovolk.dictionary.presentation.navigation.graph.MainTabRotes
import com.ovolk.dictionary.presentation.navigation.stack.CommonRotes

@Composable
fun ExamScreen(
    navController: NavHostController,
    listName: String,
    listId: Long,
) {

    val viewModel = hiltViewModel<ExamKnowledgeWordsViewModel>()
    val state = viewModel.composeState
    val onAction = viewModel::onAction


    // temporary solution for updating exam list after create first word
    LaunchedEffect(state.shouldLoadWordListAgain) {
        if (state.examWordList.isEmpty() && state.shouldLoadWordListAgain) {
            onAction(ExamAction.LoadExamList(listId = listId, listName = listName))
        }
    }

    LaunchedEffect(listName, listId) {
        onAction(ExamAction.LoadExamList(listId = listId, listName = listName))
    }

    if (viewModel.listener == null) {
        viewModel.listener = object : ExamKnowledgeWordsViewModel.Listener {
            override fun onNavigateToCreateFirstWord() {
                navController.navigate("${CommonRotes.MODIFY_WORD}/mode=${ModifyWordModes.MODE_ADD}?listId=${listId}")
            }

            override fun onNavigateToHome() {
                navController.navigate("${MainTabRotes.HOME}")
            }
        }
    }

    ExamPresenter(state = state, onAction = onAction)
}