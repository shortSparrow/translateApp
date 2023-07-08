package com.ovolk.dictionary.presentation.word_list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ovolk.dictionary.domain.use_case.word_list.GetSearchedWordListUseCase
import com.ovolk.dictionary.domain.word_audio.WordListAudioPlayer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class WordListViewModel @Inject constructor(
    private val getSearchedWordListUseCase: GetSearchedWordListUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    var listener: Listener? = null
    var state by mutableStateOf(WordListState())
        private set
    private val wordListAudioPlayer  =  WordListAudioPlayer()

    private var searchJob: Job? = null

    init {
        val searchedWord: String? = savedStateHandle["searchedWord"]
        if (searchedWord != null) {
            state = state.copy(searchValue = searchedWord)
        }
        searchDebounced(searchedWord ?: "")
    }

    fun onAction(action: WordListAction) {
        when (action) {
            WordListAction.OnPressAddNewWord -> listener?.navigateToCreateNewWord()
            is WordListAction.OnPressWordItem -> listener?.navigateToExistingWord(action.wordId)
            is WordListAction.SearchWord -> {
                state = state.copy(searchValue = action.value)
                searchDebounced(action.value)
            }

            is WordListAction.PlayAudio -> {
                wordListAudioPlayer.playAudio(
                    onCompletionListener = action.onCompletionListener,
                    onStartListener = action.onStartListener,
                    word = action.word,
                )
            }
        }
    }

    private suspend fun searchWord(searchValue: String) {
        getSearchedWordListUseCase.getWords(searchValue)
            .collectLatest {
                val list = it.list
                    .run {
                        if (searchValue.isEmpty()) this else this.sortedWith(compareBy { l -> l.value.length })
                    }

                withContext(Dispatchers.Main) {
                    state = state.copy(
                        filteredWordList = list,
                        totalWordListSize = it.total,
                        isLoading = false
                    )
                }
            }
    }

    private fun searchDebounced(searchText: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch(Dispatchers.IO) {
            searchWord(searchText)
        }
    }

    interface Listener {
        fun navigateToCreateNewWord()
        fun navigateToExistingWord(wordId: Long)
    }

}