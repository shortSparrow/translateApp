package com.ovolk.dictionary.presentation.exam

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavHostController
import com.ovolk.dictionary.R
import com.ovolk.dictionary.data.in_memory_storage.ExamLocalCache
import com.ovolk.dictionary.data.in_memory_storage.ExamStatus
import com.ovolk.dictionary.presentation.core.dialog.ConfirmDialog
import com.ovolk.dictionary.presentation.exam.components.ExamPresenter
import com.ovolk.dictionary.presentation.modify_word.ModifyWordModes
import com.ovolk.dictionary.presentation.navigation.bottomTabNavigate
import com.ovolk.dictionary.presentation.navigation.graph.MainTabBottomBar
import com.ovolk.dictionary.presentation.navigation.graph.MainTabRotes
import com.ovolk.dictionary.presentation.navigation.stack.CommonRotes
import com.ovolk.dictionary.util.compose.BackHandler

@Composable
fun ExamScreen(navController: NavHostController) {
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

//    lister when user go back from createFirstWord or createActiveDictionary and launch loading again
    val lifecycleOwner = rememberUpdatedState(LocalLifecycleOwner.current)
    DisposableEffect(lifecycleOwner.value) {
        val lifecycle = lifecycleOwner.value.lifecycle
        val observer = LifecycleEventObserver { owner, event ->
            if(event == Lifecycle.Event.ON_CREATE && state.shouldLoadWordListAgain) {
                onAction(ExamAction.ReloadLoadExamList)
            }
        }

        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
        }
    }



    BackHandler {
        if (examLocalCache.examStatus == ExamStatus.IN_PROGRESS) {
            examLocalCache.setIsInterruptExamPopupShown(true)
        } else {
            navController.popBackStack()
        }
    }

    if (viewModel.listener == null) {
        viewModel.listener = object : ExamKnowledgeWordsViewModel.Listener {
            override fun onNavigateToCreateFirstWord(listId: Long?) {
                navController.navigate("${CommonRotes.MODIFY_WORD}/mode=${ModifyWordModes.MODE_ADD}?listId=${listId}")
            }

            override fun onNavigateToHome() {
                navController.navigate(MainTabBottomBar.Home.route) {
                    popUpTo(MainTabBottomBar.Home.route) { inclusive = true }
                }
            }

            override fun goToDictionaryList() {
                navController.navigate("${CommonRotes.DICTIONARY_LIST}")
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
                } ?: run {
                    examLocalCache.resetToDefault()
                    navController.popBackStack()
                }
            },
            onDeclineClick = { examLocalCache.setIsInterruptExamPopupShown(false) })
    }

    ExamPresenter(state = state, onAction = onAction)
}