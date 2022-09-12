package com.example.ttanslateapp.presentation.lists

import androidx.navigation.NavController

sealed class ListsAction {
    object DeleteSelectedLists : ListsAction()
    object ConfirmDeleteSelectedLists : ListsAction()
    object DeclineDeleteSelectedLists : ListsAction()
    object CloseModal : ListsAction()
    object OpenModalNewList : ListsAction()
    data class OpenModalRenameList( val currentName: String, val listId: Long) : ListsAction()
    data class SelectList(val listId: Long) : ListsAction()
    data class AddNewList(val title: String) : ListsAction()
    data class RenameList(val title: String) : ListsAction()
    data class OnListItemPress(val listId: Long, val navController: NavController) : ListsAction()
}
