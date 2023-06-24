package com.ovolk.dictionary.presentation.settings_dictionaries

import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ovolk.dictionary.domain.response.Either
import com.ovolk.dictionary.domain.use_case.modify_dictionary.CrudDictionaryUseCase
import com.ovolk.dictionary.presentation.DictionaryApp
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
            dictionaryUseCase.getDictionaryList().collectLatest { dictionaryList ->
                state = state.copy(dictionaryList = dictionaryList)
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
                    val listToDelete = state.dictionaryList.filter { it.isSelected }.map { it.id }

                    when (val response = dictionaryUseCase.deleteDictionaries(listToDelete)) {
                        is Either.Failure -> {
                            Toast.makeText(
                                DictionaryApp.applicationContext(),
                                response.value.message,
                                Toast.LENGTH_SHORT
                            ).show()
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