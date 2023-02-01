package com.ovolk.dictionary.presentation.exam

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import com.ovolk.dictionary.presentation.exam.components.ExamPresenter
import com.ovolk.dictionary.presentation.modify_word.ModifyWordModes
import com.ovolk.dictionary.presentation.navigation.graph.MainTabBottomBar
import com.ovolk.dictionary.presentation.navigation.graph.MainTabRotes
import com.ovolk.dictionary.presentation.navigation.graph.SelectLanguagesScreens
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
    val currentDestination = navController.currentDestination

    LaunchedEffect(currentDestination) {
        currentDestination?.route?.let {
            if (!it.startsWith(MainTabRotes.EXAM.name)) {
                // leave Exam tab
                viewModel.updateAnsweredWords()
            }
        }
    }


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
                navController.navigate(MainTabBottomBar.Home.route) {
                    popUpTo(MainTabBottomBar.Home.route) { inclusive = true }
                }
            }
        }
    }

    ExamPresenter(state = state, onAction = onAction)
}