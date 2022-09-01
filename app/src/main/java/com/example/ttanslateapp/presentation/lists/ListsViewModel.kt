package com.example.ttanslateapp.presentation.lists

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ttanslateapp.domain.use_case.lists.AddNewListUseCase
import com.example.ttanslateapp.domain.use_case.lists.GetAllListsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListsViewModel @Inject constructor(
    private val getAllListsUseCase: GetAllListsUseCase,
    private val addNewListUseCase: AddNewListUseCase
) :ViewModel() {
    var state by mutableStateOf(ListsState())
        private set

    init {
        viewModelScope.launch {
            getAllListsUseCase.getAllLists().collectLatest { list ->
                state = state.copy(list = list)
            }
        }
    }

    fun onAction(action:ListsAction) {
        when(action) {
            is ListsAction.AddNewList -> TODO()
            ListsAction.OpenAddAllListsPopup -> {
                viewModelScope.launch {
                    addNewListUseCase.addNewList("first")
                }
            }
            ListsAction.GetAllLists -> {

            }
        }
    }
}