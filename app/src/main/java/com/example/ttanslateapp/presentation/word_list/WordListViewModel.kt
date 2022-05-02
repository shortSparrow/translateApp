package com.example.ttanslateapp.presentation.word_list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ttanslateapp.domain.model.WordRV
import com.example.ttanslateapp.domain.use_case.GetSearchedWordListUseCase
import com.example.ttanslateapp.domain.use_case.GetWordListUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class WordListViewModel @Inject constructor(
    private val getWordListUseCase: GetWordListUseCase,
    private val getSearchedWordListUseCase: GetSearchedWordListUseCase
) : ViewModel() {
    private val _wordList = MutableLiveData<List<WordRV>>()
    val wordList = _wordList
    private var searchJob: Job? = null

    // FIXME observeForever look strange, maybe delete
    fun loadWordList() {
        viewModelScope.launch {
            getWordListUseCase().observeForever {
                wordList.value = it
            }
        }
    }


    private fun searchWord(value: String) {
        if (value.isEmpty()) {
            loadWordList()
        }

        viewModelScope.launch {
            getSearchedWordListUseCase(value).observeForever {
                wordList.value = it
            }
        }
    }


    fun searchDebounced(searchText: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(500)
            searchWord(searchText)
        }
    }
}