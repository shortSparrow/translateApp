package com.ovolk.dictionary.presentation.lists

import com.ovolk.dictionary.domain.SimpleError
import com.ovolk.dictionary.domain.model.lists.ListItem
import com.ovolk.dictionary.presentation.list_full.LoadingState

data class ListsState(
    val list: List<ListItem> = emptyList(),
    val isLoadingList: LoadingState = LoadingState.IDLE,
    val modalList: ModalListState = ModalListState(),
    val isOpenDeleteListModal: Boolean = false,
    val modalError: SimpleError = SimpleError(),
)

sealed class ListsAction {
    object DeleteSelectedLists : ListsAction()
    object ConfirmDeleteSelectedLists : ListsAction()
    object DeclineDeleteSelectedLists : ListsAction()
    object CloseModal : ListsAction()
    object OpenModalNewList : ListsAction()
    object ResetModalError : ListsAction()
    data class OpenModalRenameList(val currentName: String, val listId: Long) : ListsAction()
    data class SelectList(val listId: Long) : ListsAction()
    data class AddNewList(val title: String) : ListsAction()
    data class RenameList(val title: String) : ListsAction()
    data class OnListItemPress(
        val listId: Long,
        val listName: String,
    ) : ListsAction()
}
