package com.ovolk.dictionary.presentation.exam

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.ovolk.dictionary.R
import com.ovolk.dictionary.data.in_memory_storage.ExamLocalCache
import com.ovolk.dictionary.presentation.core.dialog.ConfirmDialog
import com.ovolk.dictionary.presentation.exam.components.ExamPresenter
import com.ovolk.dictionary.presentation.modify_word.ModifyWordModes
import com.ovolk.dictionary.presentation.navigation.bottomTabNavigate
import com.ovolk.dictionary.presentation.navigation.graph.MainTabBottomBar
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
    val currentDestination = navController.currentDestination
    val examLocalCache = ExamLocalCache.getInstance()
    val isInterruptExamPopupShown = examLocalCache.isInterruptExamPopupShown.collectAsState()

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

    if (isInterruptExamPopupShown.value) {
        ConfirmDialog(
            message = stringResource(id = R.string.exam_interrupt),
            onAcceptClick = {
                examLocalCache.interruptedRoute?.let { interruptedRoute ->
                    examLocalCache.resetToDefault()
                    bottomTabNavigate(navController = navController, route = interruptedRoute)
                }
            },
            onDeclineClick = { examLocalCache.setIsInterruptExamPopupShown(false) })
    }

    ExamPresenter(state = state, onAction = onAction)
}