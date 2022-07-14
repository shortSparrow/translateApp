package com.example.ttanslateapp.presentation.word_list

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ttanslateapp.domain.model.WordRV
import com.example.ttanslateapp.domain.use_case.DeleteWordUseCase
import com.example.ttanslateapp.domain.use_case.GetSearchedWordListUseCase
import com.example.ttanslateapp.presentation.exam.adapter.ExamKnowledgeUiState
import com.example.ttanslateapp.presentation.exam.adapter.ExamMode
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


sealed interface WordListViewModelState {
    data class IsLoading(val isLoading: Boolean) : WordListViewModelState
    data class LoadSuccess(val wordList: List<WordRV>, val dictionaryIsEmpty: Boolean) :
        WordListViewModelState
}

data class WordListState(
    val wordList: List<WordRV> = emptyList()
)

class WordListViewModel @Inject constructor(
    private val getSearchedWordListUseCase: GetSearchedWordListUseCase,
) : ViewModel() {
    private val _uiState = MutableLiveData<WordListViewModelState>()
    val uiState: LiveData<WordListViewModelState> = _uiState

    private var state = WordListState()

    private var searchJob: Job? = null
    private var dictionaryIsEmpty = false

    var searchInputValue = ""

    init {
        _uiState.value = WordListViewModelState.IsLoading(true)
        searchDebounced("")
    }

    private suspend fun searchWord(searchValue: String) {
        getSearchedWordListUseCase(searchValue)
            .collect {
                val list = it
                    .map { it.copy(translates = it.translates.filter { it.isHidden == false }) }
                    .apply {
                        if (searchValue.isEmpty()) it else it.sortedBy { it.value.length }
                    }

                if (searchValue.isEmpty()) {
                    dictionaryIsEmpty = it.isEmpty()
                }
                state = state.copy(wordList = list)
                _uiState.value = WordListViewModelState.LoadSuccess(
                    wordList = list,
                    dictionaryIsEmpty = dictionaryIsEmpty
                )
            }
    }

    fun searchDebounced(searchText: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            searchWord(searchText)
        }
    }

}