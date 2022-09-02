package com.example.ttanslateapp.presentation.lists

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ttanslateapp.domain.use_case.lists.AddNewListUseCase
import com.example.ttanslateapp.domain.use_case.lists.DeleteListsUseCase
import com.example.ttanslateapp.domain.use_case.lists.GetAllListsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


enum class ModalType { NEW, RENAME }
data class ModalListState(
    val isOpen: Boolean = false,
    val type: ModalType? = null,
    val initialValue: String = "",
    val title: String = "",
)


@HiltViewModel
class ListsViewModel @Inject constructor(
    private val getAllListsUseCase: GetAllListsUseCase,
    private val addNewListUseCase: AddNewListUseCase,
    private val deleteListsUseCase: DeleteListsUseCase
) : ViewModel() {
    var state by mutableStateOf(ListsState())
        private set

    init {
        viewModelScope.launch {
            getAllListsUseCase.getAllLists().collectLatest { list ->
                state =
                    state.copy(list = list.map {
                        it.copy(
                            isSelected = state.list.find { oldItem -> oldItem.id == it.id }?.isSelected
                                ?: false
                        )
                    })
            }
        }
    }

    fun closeModalList() {
        state = state.copy(
            modalList = ModalListState(isOpen = false)
        )
    }

    fun onAction(action: ListsAction) {
        when (action) {
            is ListsAction.AddNewList -> {
                viewModelScope.launch {
                    addNewListUseCase.addNewList(action.title)
                }
                closeModalList()
            }
            ListsAction.OpenAddAllListsPopup -> {
                // delete
            }
            ListsAction.GetAllLists -> {

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
            ListsAction.DeletedSelectedLists -> {
                val deletedListsId = state.list.filter { it.isSelected }.map { it.id }
                viewModelScope.launch {
                    deleteListsUseCase.deleteLists(deletedListsId)
                }
            }
            is ListsAction.OpenModal -> {
                state = state.copy(
                    modalList = ModalListState(
                        isOpen = true,
                        type = action.type,
                        title = "Add New List"
                    )
                )
            }
        }
    }
}