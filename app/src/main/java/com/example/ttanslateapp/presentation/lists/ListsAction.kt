package com.example.ttanslateapp.presentation.lists

sealed class ListsAction {
    object DeletedSelectedLists : ListsAction()
    data class OpenModal(val type: ModalType) : ListsAction()
    data class SelectList(val listId: Long) : ListsAction()
    data class AddNewList(val title: String) : ListsAction()
}
