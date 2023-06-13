package com.ovolk.dictionary.presentation.settings_dictionaries

import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ovolk.dictionary.domain.response.Either
import com.ovolk.dictionary.domain.response.FailureMessage
import com.ovolk.dictionary.domain.use_case.modify_dictionary.CrudDictionaryUseCase
import com.ovolk.dictionary.domain.use_case.modify_dictionary.SetActiveDictionary
import com.ovolk.dictionary.presentation.DictionaryApp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SettingsDictionariesViewModel @Inject constructor(
    private val dictionaryUseCase: CrudDictionaryUseCase,
    private val setActiveDictionary: SetActiveDictionary
) : ViewModel() {
    var state by mutableStateOf(SettingsDictionariesState())
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

    fun onAction(action: SettingsDictionariesAction) {
        when (action) {
            is SettingsDictionariesAction.OnPressDictionary -> {
                viewModelScope.launch {
                    val response = setActiveDictionary.setDictionaryActive(
                        dictionaryId = action.id,
                        isActive = true
                    )

                    when (response) {
                        is Either.Failure -> {
                            if (response.value is FailureMessage) {
                                Toast.makeText(
                                    DictionaryApp.applicationContext(),
                                    response.value.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        is Either.Success -> {}
                    }
                }
            }

            is SettingsDictionariesAction.OnSelectDictionary -> {
                val newList = state.dictionaryList.map {
                    if (it.id == action.id) {
                        return@map it.copy(isSelected = !it.isSelected)
                    }
                    return@map it.copy(isSelected = false)
                }

                state = state.copy(dictionaryList = newList)
            }

            SettingsDictionariesAction.AddNewDictionary -> {
                listener?.navigate(null)
            }

            SettingsDictionariesAction.EditDictionary -> {
                val selectedDictionary = state.dictionaryList.find { it.isSelected }
                selectedDictionary?.let {
                    listener?.navigate(it.id)
                }
            }

            SettingsDictionariesAction.DeleteDictionary -> {
                viewModelScope.launch {
                    state.dictionaryList.find { it.isSelected }?.id?.let { id ->
                        when (val response = dictionaryUseCase.deleteDictionary(id)) {
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
            }
        }
    }

    interface Listener {
        fun navigate(dictionaryId: Long?)
    }
}