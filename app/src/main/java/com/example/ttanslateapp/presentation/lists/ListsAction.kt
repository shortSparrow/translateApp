package com.example.ttanslateapp.presentation.lists

import com.example.ttanslateapp.domain.model.lists.ListItem

sealed class ListsAction {
    object GetAllLists : ListsAction()
    object OpenAddAllListsPopup : ListsAction()
    data class AddNewList(val newItem: ListItem) : ListsAction()
}
