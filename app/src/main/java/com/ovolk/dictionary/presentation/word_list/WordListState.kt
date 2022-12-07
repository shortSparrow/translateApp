package com.ovolk.dictionary.presentation.word_list

import androidx.compose.runtime.mutableStateListOf
import com.ovolk.dictionary.domain.model.modify_word.WordRV

sealed interface WordListAction {
    data class SearchWord(val value: String) : WordListAction
    object OnPressAddNewWord : WordListAction
    data class OnPressWordItem(val wordId: Long) : WordListAction
    data class PlayAudio(
        val word: WordRV,
        val onStartListener: () -> Unit,
        val onCompletionListener: () -> Unit
    ) : WordListAction
}

data class WordListState(
    val searchValue: String = "",
    val isLoading: Boolean = true,
    val totalWordListSize: Int = 0,
    val filteredWordList: List<WordRV> = mutableStateListOf(),
)
