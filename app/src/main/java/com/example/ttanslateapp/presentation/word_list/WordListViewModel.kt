package com.example.ttanslateapp.presentation.word_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ttanslateapp.domain.model.WordRV
import com.example.ttanslateapp.domain.use_case.GetSearchedWordListUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed interface WordListViewModelState {
    object IsLoading : WordListViewModelState
    data class LoadSuccess(
        val wordList: List<WordRV>,
        val dictionaryIsEmpty: Boolean,
        val isRestoreUi: Boolean = false
    ) :
        WordListViewModelState
}

data class WordListState(
    val wordList: List<WordRV> = emptyList(),
    val isLoading: Boolean = true,
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
        _uiState.value = WordListViewModelState.IsLoading
        searchDebounced("")
    }

    // if we visited screen in the past in our session restore ui, if it is the first time - ignore
    fun restoreUI() {
        if (state.isLoading) {
            _uiState.value = WordListViewModelState.IsLoading
        } else {
            _uiState.value = WordListViewModelState.LoadSuccess(
                wordList = state.wordList,
                dictionaryIsEmpty = dictionaryIsEmpty,
                isRestoreUi = true
            )
        }

    }

    private suspend fun searchWord(searchValue: String) {
        getSearchedWordListUseCase(searchValue)
            .collectLatest {
                val list = it
                    .map { it.copy(translates = it.translates.filter { it.isHidden == false }) }
                    .apply {
                        if (searchValue.isEmpty()) it else it.sortedBy { it.value.length }
                    }

                if (searchValue.isEmpty()) {
                    dictionaryIsEmpty = it.isEmpty()
                }
                state = state.copy(wordList = list, isLoading = false)
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