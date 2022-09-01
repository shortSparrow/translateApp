package com.example.ttanslateapp.presentation.word_list

import android.app.Application
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ttanslateapp.R
import com.example.ttanslateapp.domain.model.WordRV
import com.example.ttanslateapp.domain.use_case.DeleteWordUseCase
import com.example.ttanslateapp.domain.use_case.GetSearchedWordListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


sealed interface WordListViewModelState {
    object IsLoading : WordListViewModelState
    data class LoadSuccess(
        val wordList: List<WordRV>,
        val dictionaryIsEmpty: Boolean,
        val isRestoreUi: Boolean = false,
        val shouldScrollToStart: Boolean = false,
    ) :
        WordListViewModelState
}

data class WordListState(
    val wordList: List<WordRV> = emptyList(),
    val isLoading: Boolean = true,
)

@HiltViewModel
class WordListViewModel @Inject constructor(
    private val getSearchedWordListUseCase: GetSearchedWordListUseCase,
    private val deleteWordUseCase: DeleteWordUseCase,
    private val application: Application
) : ViewModel() {
    private val _uiState = MutableLiveData<WordListViewModelState>()
    val uiState: LiveData<WordListViewModelState> = _uiState

    private var state = WordListState()
    var previousSize = 0;

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
                isRestoreUi = true,
                shouldScrollToStart= previousSize < state.wordList.size
            )
        }

    }
    fun deleteWord(wordId: Long?) {
        if (wordId != null) {
            viewModelScope.launch {
            val deleteSuccess =   deleteWordUseCase(wordId)
                if (deleteSuccess) {
                    Toast.makeText(
                        application,
                        application.getString(R.string.modify_word_success_delete_word),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }



    private suspend fun searchWord(searchValue: String) {
        getSearchedWordListUseCase(searchValue)
            .collectLatest {
                val list = it
                    .map { it.copy(translates = it.translates.filter { it.isHidden == false }) }
                    .run {
                        if (searchValue.isEmpty()) it else it.sortedWith(compareBy { l-> l.value.length })
                    }

                if (searchValue.isEmpty()) {
                    dictionaryIsEmpty = it.isEmpty()
                }

                state = state.copy(wordList = list, isLoading = false)
                _uiState.value = WordListViewModelState.LoadSuccess(
                    wordList = list,
                    dictionaryIsEmpty = dictionaryIsEmpty,
                    shouldScrollToStart= (previousSize < list.size) || searchValue.isNotEmpty()
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