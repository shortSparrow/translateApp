package com.ovolk.dictionary.presentation.lists

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.ovolk.dictionary.presentation.lists.components.ListsPresenter
import com.ovolk.dictionary.presentation.modify_dictionary.ModifyDictionaryModes
import com.ovolk.dictionary.presentation.navigation.graph.CommonRotes


@Composable
fun ListsScreen(navController: NavHostController) {
    val viewModel = hiltViewModel<ListsViewModel>()
    val state = viewModel.state

    LaunchedEffect(Unit) {
        viewModel.listener = object : ListsViewModel.Listener {
            override fun navigateToFullList(listId: Long, listName: String, dictionaryId: Long?) {
                navController.navigate("${CommonRotes.FULL_LIST}?listId=${listId}&dictionaryId=${dictionaryId}")
            }

            override fun toAddNewDictionary() {
                navController.navigate("${CommonRotes.MODIFY_DICTIONARY}/mode=${ModifyDictionaryModes.MODE_ADD}")
            }
        }
    }

    ListsPresenter(
        state = state,
        onAction = viewModel::onAction,
    )
}