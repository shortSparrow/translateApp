package com.example.ttanslateapp.presentation.lists

import androidx.navigation.NavController

sealed class ListsAction {
    object DeletedSelectedLists : ListsAction()
    object CloseModal : ListsAction()
    data class OpenModal(val type: ModalType) : ListsAction()
    data class SelectList(val listId: Long) : ListsAction()
    data class AddNewList(val title: String) : ListsAction()
    data class OnListItemPress(val listId: Long, val navController: NavController) : ListsAction()
}
