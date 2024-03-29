package com.ovolk.dictionary.presentation.list_full

import com.ovolk.dictionary.domain.LoadingState
import com.ovolk.dictionary.domain.model.modify_word.WordRV


data class ListFullState(
    val listId: Long = -1L,
    val dictionaryId: Long = -1L,
    val listName: String = "",
    val wordList: List<WordRV> = emptyList(),
    val noAnyWords: Boolean = false,
    val loadingStatusWordList: LoadingState = LoadingState.IDLE
)

sealed class ListFullAction {
    object TakeExam : ListFullAction()
    object AddNewWord : ListFullAction()
    data class SearchWord(val query: String) : ListFullAction()
    data class PressOnWord(val wordId: Long) : ListFullAction()
    data class InitialLoadData(val listId: Long, val dictionaryId: Long) : ListFullAction()
    data class PlayAudio(
        val word: WordRV,
        val onStartListener: () -> Unit,
        val onCompletionListener: () -> Unit
    ) : ListFullAction()
}
