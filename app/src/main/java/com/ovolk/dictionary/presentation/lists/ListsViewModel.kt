package com.ovolk.dictionary.presentation.lists

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ovolk.dictionary.R
import com.ovolk.dictionary.domain.SimpleError
import com.ovolk.dictionary.domain.use_case.lists.AddNewListUseCase
import com.ovolk.dictionary.domain.use_case.lists.DeleteListsUseCase
import com.ovolk.dictionary.domain.use_case.lists.GetListsUseCase
import com.ovolk.dictionary.domain.use_case.lists.RenameListUseCase
import com.ovolk.dictionary.presentation.list_full.LoadingState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


enum class ModalType { NEW, RENAME }
data class ModalListState(
    val isOpen: Boolean = false,
    val type: ModalType? = null,
    val initialValue: String = "",
    val title: String = "",
    val listId: Long? = null
)


@HiltViewModel
class ListsViewModel @Inject constructor(
    private val getListsUseCase: GetListsUseCase,
    private val addNewListUseCase: AddNewListUseCase,
    private val deleteListsUseCase: DeleteListsUseCase,
    private val renameListUseCase: RenameListUseCase,
    private val application: Application
) : ViewModel() {
    var state by mutableStateOf(ListsState())
        private set

    init {
        viewModelScope.launch {
            state = state.copy(isLoadingList = LoadingState.PENDING)
            getListsUseCase.getAllLists().collectLatest { list ->
                state =
                    state.copy(
                        list = list.map {
                            it.copy(
                                isSelected = state.list.find { oldItem -> oldItem.id == it.id }?.isSelected
                                    ?: false
                            )
                        },
                        isLoadingList = LoadingState.SUCCESS
                    )
            }
        }
    }

    private fun closeModalList() {
        state = state.copy(
            modalList = ModalListState(isOpen = false, listId = null),
            modalError = SimpleError(isError = false)
        )
    }

    private fun isOpenConfirmDeleteListDialog(value: Boolean) {
        state = state.copy(isOpenDeleteListModal = value)
    }

    private fun handleModalList(
        text: String,
        onSuccess: (title: String) -> Unit
    ) {
        val title = text.trim()
        if (title.isNotEmpty()) {
            onSuccess(title)
        } else {
            state = state.copy(
                modalError = SimpleError(
                    isError = true,
                    text = application.getString(R.string.lists_screen_modal_error)
                )
            )
        }
    }

    private fun resetModalError() {
        if (state.modalError.isError) {
            state = state.copy(modalError = SimpleError(isError = false))
        }
    }

    fun onAction(action: ListsAction) {
        when (action) {
            is ListsAction.AddNewList -> {
                handleModalList(
                    action.title,
                    onSuccess = { title ->
                        viewModelScope.launch {
                            addNewListUseCase.addNewList(title)
                        }
                        closeModalList()
                    }
                )
            }
            is ListsAction.SelectList -> {
                val newList = state.list.map { item ->
                    if (item.id == action.listId) {
                        return@map item.copy(isSelected = !item.isSelected)
                    }
                    return@map item
                }
                state = state.copy(list = newList)
            }
            ListsAction.DeleteSelectedLists -> {
                isOpenConfirmDeleteListDialog(true)
            }
            ListsAction.ConfirmDeleteSelectedLists -> {
                val deletedListsId = state.list.filter { it.isSelected }.map { it.id }
                viewModelScope.launch {
                    deleteListsUseCase.deleteLists(deletedListsId)
                }
                isOpenConfirmDeleteListDialog(false)
            }
            ListsAction.DeclineDeleteSelectedLists -> {
                isOpenConfirmDeleteListDialog(false)
            }
            is ListsAction.OnListItemPress -> {
                action.navController.navigate(
                    ListFragmentDirections.actionListFragmentToListFull(
                        listId = action.listId,
                        listName = action.listName
                    )
                )
            }
            ListsAction.CloseModal -> {
                closeModalList()
            }
            ListsAction.OpenModalNewList -> {
                state = state.copy(
                    modalList = ModalListState(
                        isOpen = true,
                        type = ModalType.NEW,
                        title = application.getString(R.string.lists_screen_add_new_list_dialog_title),
                        listId = null
                    )
                )
            }
            is ListsAction.OpenModalRenameList -> {
                state = state.copy(
                    modalList = ModalListState(
                        isOpen = true,
                        type = ModalType.RENAME,
                        title = application.getString(R.string.lists_screen_rename_list_dialog_title),
                        initialValue = action.currentName,
                        listId = action.listId,
                    )
                )
            }
            is ListsAction.RenameList -> {
                handleModalList(
                    action.title,
                    onSuccess = { title ->
                        viewModelScope.launch {
                            state.modalList.listId?.let {
                                renameListUseCase.addNewList(
                                    title = title,
                                    id = it
                                )
                            }
                            closeModalList()
                        }
                    }
                )
            }
            ListsAction.ResetModalError -> {
                resetModalError()
            }
        }
    }
}