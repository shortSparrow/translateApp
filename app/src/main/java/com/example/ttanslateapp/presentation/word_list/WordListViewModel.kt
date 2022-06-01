package com.example.ttanslateapp.presentation.word_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ttanslateapp.domain.model.WordRV
import com.example.ttanslateapp.domain.use_case.DeleteWordUseCase
import com.example.ttanslateapp.domain.use_case.GetSearchedWordListUseCase
import com.example.ttanslateapp.domain.use_case.GetWordListUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class WordListViewModel @Inject constructor(
    private val getWordListUseCase: GetWordListUseCase,
    private val getSearchedWordListUseCase: GetSearchedWordListUseCase,
    private val deleteWordUseCase: DeleteWordUseCase
) : ViewModel() {
    // FIXME: a lot of invokes, and adapter shadow looks strange
    private val _wordList = MutableLiveData<List<WordRV>>()
    val wordList = _wordList
    private var searchJob: Job? = null

    private val _dictionaryIsEmpty = MutableLiveData<Boolean>()
    val dictionaryIsEmpty: LiveData<Boolean> = _dictionaryIsEmpty

    fun deleteWordById(wordId: Long) {
        viewModelScope.launch {
            deleteWordUseCase(wordId)
        }
    }

    fun loadWordList() {
        viewModelScope.launch {
            getWordListUseCase()
                .collect {
                    _wordList.value = it
                    _dictionaryIsEmpty.value = it.isEmpty()
                }
        }
    }

    private suspend fun searchWord(searchValue: String) {
        if (searchValue.isEmpty()) {
            loadWordList()
        }
        getSearchedWordListUseCase(searchValue)
            .collect {
                wordList.value = it
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