package com.ovolk.dictionary.presentation.lists

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.appcompattheme.AppCompatTheme
import com.ovolk.dictionary.presentation.lists.components.ListsPresenter
import com.ovolk.dictionary.presentation.navigation.graph.CommonRotes
import com.ovolk.dictionary.presentation.navigation.graph.CommonScreen


@Composable
fun ListsScreen(navController: NavHostController) {
    val viewModel = hiltViewModel<ListsViewModel>()
    val state = viewModel.state

    if (viewModel.listener == null) {
        viewModel.listener = object : ListsViewModel.Listener {
            override fun navigateToFullList(listId: Long, listName: String) {
                navController.navigate("${CommonRotes.FULL_LIST}?listId=${listId}&listName=${listName}")
            }
        }
    }

    ListsPresenter(
        state = state,
        onAction = viewModel::onAction,
    )
}