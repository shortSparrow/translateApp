package com.ovolk.dictionary.presentation.dictionary_list

import com.ovolk.dictionary.domain.LoadingState
import com.ovolk.dictionary.domain.model.dictionary.SelectableDictionary

data class DictionaryListState(
    val loadingState: LoadingState = LoadingState.IDLE,
    val dictionaryList: List<SelectableDictionary> = emptyList(),
    val isDeleteDictionaryModalOpen: Boolean = false,
)

sealed interface DictionaryListAction {
    data class OnSelectDictionary(val id: Long) : DictionaryListAction
    data class OnPressDictionary(val id: Long) : DictionaryListAction
    data class ToggleOpenDeleteDictionaryModal(val isOpen: Boolean) : DictionaryListAction
    object DeleteDictionary : DictionaryListAction
    object EditDictionary : DictionaryListAction
    object AddNewDictionary : DictionaryListAction
}