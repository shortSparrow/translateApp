package com.example.ttanslateapp.presentation.word_list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ttanslateapp.domain.use_case.GetWordListUseCase
import javax.inject.Inject

class WordListViewModel @Inject constructor(
    private val getWordListUseCase: GetWordListUseCase
) : ViewModel() {
    val wordList = getWordListUseCase()


}