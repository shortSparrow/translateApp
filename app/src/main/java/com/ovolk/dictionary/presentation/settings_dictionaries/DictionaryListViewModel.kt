package com.ovolk.dictionary.presentation.settings_dictionaries

import androidx.compose.material.SnackbarDuration
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ovolk.dictionary.domain.LoadingState
import com.ovolk.dictionary.domain.response.Either
import com.ovolk.dictionary.domain.snackbar.GlobalSnackbarManger
import com.ovolk.dictionary.domain.use_case.modify_dictionary.CrudDictionaryUseCase
import com.ovolk.dictionary.presentation.core.snackbar.SnackBarError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DictionaryListViewModel @Inject constructor(
    private val dictionaryUseCase: CrudDictionaryUseCase,
) : ViewModel() {
    var state by mutableStateOf(DictionaryListState())
        private set

    var listener: Listener? = null

    init {
        getDictionaries()
    }

    private fun getDictionaries() {
        viewModelScope.launch {
            dictionaryUseCase.getSelectableDictionaryList().collectLatest { dictionaryList ->
                state = state.copy(dictionaryList = dictionaryList, loadingState = LoadingState.SUCCESS)
            }
        }
    }

    fun onAction(action: DictionaryListAction) {
        when (action) {
            is DictionaryListAction.OnPressDictionary -> {
                listener?.goToDictionaryWords(action.id)
            }

            is DictionaryListAction.OnSelectDictionary -> {
                val newList = state.dictionaryList.map {
                    if (it.id == action.id) {
                        return@map it.copy(isSelected = !it.isSelected)
                    }
                    return@map it
                }

                state = state.copy(dictionaryList = newList)
            }

            DictionaryListAction.AddNewDictionary -> {
                listener?.goToModifyDictionary(null)
            }

            DictionaryListAction.EditDictionary -> {
                val selectedDictionary = state.dictionaryList.find { it.isSelected }
                selectedDictionary?.let {
                    listener?.goToModifyDictionary(it.id)
                }
            }

            DictionaryListAction.DeleteDictionary -> {
                state = state.copy(isDeleteDictionaryModalOpen = false)
                viewModelScope.launch {
                    val listToDelete = state.dictionaryList.filter { it.isSelected }
                    val listIdToDelete = listToDelete.map { it.id }
                    val isActiveExist = listToDelete.find { it.isActive } != null

                    val response = dictionaryUseCase.deleteDictionaries(
                        listIdToDelete,
                        isActiveExist = isActiveExist
                    )
                    when (response) {
                        is Either.Failure -> {
                            GlobalSnackbarManger.showGlobalSnackbar(
                                duration = SnackbarDuration.Short,
                                data = SnackBarError(message = response.value.message),
                            )
                        }

                        is Either.Success -> {}
                    }
                }
            }

            is DictionaryListAction.ToggleOpenDeleteDictionaryModal -> {
                state = state.copy(isDeleteDictionaryModalOpen = action.isOpen)
            }

        }
    }

    interface Listener {
        fun goToModifyDictionary(dictionaryId: Long?)
        fun goToDictionaryWords(dictionaryId: Long)
    }
}