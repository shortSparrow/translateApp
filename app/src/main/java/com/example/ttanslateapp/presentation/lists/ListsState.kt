package com.example.ttanslateapp.presentation.lists

import com.example.ttanslateapp.domain.model.lists.ListItem
import com.example.ttanslateapp.presentation.list_full.LoadingState

data class ListsState(
    val list: List<ListItem> = emptyList(),
    val isLoadingList: LoadingState = LoadingState.IDLE,
    val modalList: ModalListState = ModalListState()
)
