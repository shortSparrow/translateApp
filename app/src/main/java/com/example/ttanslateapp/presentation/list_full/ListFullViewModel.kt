package com.example.ttanslateapp.presentation.list_full

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ListsViewModel @Inject constructor(

) : ViewModel() {
    var state by mutableStateOf(ListFullState())
        private set

    init {
        viewModelScope.launch {

        }
    }


    fun onAction(action: ListFullAction) {
        when (action) {
            ListFullAction.GoBack -> TODO()
            is ListFullAction.SearchWord -> TODO()
            ListFullAction.TakeExam -> TODO()
            is ListFullAction.PressOnWord -> TODO()
            ListFullAction.AddNewWord -> TODO()
        }
    }
}