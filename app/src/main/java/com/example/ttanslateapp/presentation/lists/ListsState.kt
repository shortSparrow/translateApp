package com.example.ttanslateapp.presentation.lists

import com.example.ttanslateapp.domain.model.lists.ListItem

data class ListsState(
    val list: List<ListItem> = emptyList(),
    val isLoadingList: String = "idle",
    val isFailedLoad: Any? = null,
    val modalList: ModalListState = ModalListState()
)
