package com.ovolk.dictionary.presentation.lists

import android.app.Application
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ovolk.dictionary.R
import com.ovolk.dictionary.domain.SimpleError
import com.ovolk.dictionary.domain.response.Either
import com.ovolk.dictionary.domain.response.Failure
import com.ovolk.dictionary.domain.response.FailureMessage
import com.ovolk.dictionary.domain.response.FailureWithCode
import com.ovolk.dictionary.domain.use_case.lists.AddNewListUseCase
import com.ovolk.dictionary.domain.use_case.lists.DeleteListsUseCase
import com.ovolk.dictionary.domain.use_case.lists.GetListsUseCase
import com.ovolk.dictionary.domain.use_case.lists.RenameListUseCase
import com.ovolk.dictionary.domain.use_case.modify_dictionary.CrudDictionaryUseCase
import com.ovolk.dictionary.domain.use_case.modify_dictionary.GetActiveDictionary
import com.ovolk.dictionary.domain.use_case.modify_dictionary.UNKNOWN_ERROR
import com.ovolk.dictionary.presentation.list_full.LoadingState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


enum class ModalType { NEW, RENAME }
data class ModalListState(
    val isOpen: Boolean = false,
    val type: ModalType? = null,
    val initialValue: String = "",
    val title: String = "",
    val listId: Long? = null,
)

@HiltViewModel
class ListsViewModel @Inject constructor(
    private val getListsUseCase: GetListsUseCase,
    private val addNewListUseCase: AddNewListUseCase,
    private val deleteListsUseCase: DeleteListsUseCase,
    private val renameListUseCase: RenameListUseCase,
    private val getActiveDictionary: GetActiveDictionary,
    private val crudDictionaryUseCase: CrudDictionaryUseCase,
    private val application: Application,
) : ViewModel() {
    var listener: Listener? = null
    var state by mutableStateOf(ListsState())
        private set

    init {
        state = state.copy(isLoadingList = LoadingState.PENDING)

        viewModelScope.launch {
            state.currentDictionary.collectLatest { dictionary ->
                dictionary?.let {
                    getListsUseCase.getAllLists(it.id).collectLatest { list ->
                        withContext(Dispatchers.Main) {
                            state = state.copy(
                                list = list.map {
                                    it.copy(
                                        isSelected = state.list.find { oldItem -> oldItem.id == it.id }?.isSelected
                                            ?: false
                                    )
                                },
                            )
                        }
                    }
                }
            }
        }

        viewModelScope.launch {
            crudDictionaryUseCase.getDictionaryList().collectLatest {
                // for case when there is no dictionaries and user create first we must setup one
                val currentDictionary = if(state.currentDictionary.value == null) it.find { it.isActive } else state.currentDictionary.value
                state.currentDictionary.value = currentDictionary
                state = state.copy(dictionaryList = it)
            }
        }

        viewModelScope.launch {
            when (val activeDictionaryResponse = getActiveDictionary.getDictionaryActive()) {
                is Either.Failure -> {
                    // TODO add snackbar
                    state = state.copy(isLoadingList = LoadingState.SUCCESS)
                }

                is Either.Success -> {
                    state.currentDictionary.value = activeDictionaryResponse.value
                    state = state.copy(isLoadingList = LoadingState.SUCCESS)
                }
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

    private fun handleModalList(response: Either<Long, Failure>) {
        when (response) {
            is Either.Failure -> {
                if (response.value is FailureMessage) {
                    state = state.copy(
                        modalError = SimpleError(
                            isError = true,
                            text = response.value.message
                        )
                    )
                }

                if (response.value is FailureWithCode && response.value.code == UNKNOWN_ERROR) {
                    // TODO add snackbar
                    Toast.makeText(application, response.value.message, Toast.LENGTH_SHORT).show()
                }
            }

            is Either.Success -> {
                closeModalList()
            }
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
                viewModelScope.launch {
                    val response = addNewListUseCase.addNewList(
                        text = action.title,
                        dictionaryId = state.currentDictionary.value?.id
                    )
                    handleModalList(response)
                }
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
                listener?.navigateToFullList(
                    listId = action.listId,
                    dictionaryId = state.currentDictionary.value?.id,
                    listName = action.listName
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
                viewModelScope.launch {
                    val response = renameListUseCase.addNewList(
                        text = action.title,
                        dictionaryId = state.modalList.listId
                    )
                    handleModalList(response)
                }
            }

            ListsAction.ResetModalError -> {
                resetModalError()
            }

            is ListsAction.OnSelectDictionary -> {
                state.currentDictionary.value =
                    state.dictionaryList.find { it.id == action.dictionaryId }
            }

            ListsAction.PressAddNewDictionary -> {
                listener?.toAddNewDictionary()
            }
        }
    }


    interface Listener {
        fun navigateToFullList(listId: Long, listName: String, dictionaryId: Long?)
        fun toAddNewDictionary()
    }
}