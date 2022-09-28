package com.ovolk.dictionary.presentation.lists

import com.ovolk.dictionary.domain.model.lists.ListItem
import com.ovolk.dictionary.presentation.list_full.LoadingState

data class ListsState(
    val list: List<ListItem> = emptyList(),
    val isLoadingList: LoadingState = LoadingState.IDLE,
    val modalList: ModalListState = ModalListState(),
    val isOpenDeleteListModal: Boolean = false
)
