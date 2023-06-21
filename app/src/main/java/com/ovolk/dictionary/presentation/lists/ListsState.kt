package com.ovolk.dictionary.presentation.lists

import com.ovolk.dictionary.domain.SimpleError
import com.ovolk.dictionary.domain.model.dictionary.Dictionary
import com.ovolk.dictionary.domain.model.lists.ListItem
import com.ovolk.dictionary.presentation.list_full.LoadingState
import kotlinx.coroutines.flow.MutableStateFlow

data class ListsState(
    val list: List<ListItem> = emptyList(),
    val isLoadingList: LoadingState = LoadingState.IDLE,
    val modalList: ModalListState = ModalListState(),
    val isOpenDeleteListModal: Boolean = false,
    val modalError: SimpleError = SimpleError(),
    val currentDictionary: MutableStateFlow<Dictionary?> = MutableStateFlow(null),
    val dictionaryList: List<Dictionary> = emptyList(),
)

sealed interface ListsAction {
    object DeleteSelectedLists : ListsAction
    object ConfirmDeleteSelectedLists : ListsAction
    object DeclineDeleteSelectedLists : ListsAction
    object CloseModal : ListsAction
    object OpenModalNewList : ListsAction
    object ResetModalError : ListsAction
    data class OpenModalRenameList(val currentName: String, val listId: Long) : ListsAction
    data class SelectList(val listId: Long) : ListsAction
    data class AddNewList(val title: String) : ListsAction
    data class RenameList(val title: String) : ListsAction
    data class OnListItemPress(
        val listId: Long,
        val listName: String,
    ) : ListsAction

    data class OnSelectDictionary(val dictionaryId: Long) : ListsAction
    object PressAddNewDictionary : ListsAction
}
