package com.example.ttanslateapp.presentation.lists

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class ListsViewModel:ViewModel() {
    var state by mutableStateOf(ListsState())
        private set

    fun onAction(action:ListsAction) {
        when(action) {
            is ListsAction.AddNewList -> {

            }
            is ListsAction.GetAllLists -> {

            }
        }
    }
}