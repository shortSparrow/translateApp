package com.ovolk.dictionary.presentation.list_full

import com.ovolk.dictionary.domain.model.modify_word.WordRV

sealed class ListFullAction {
    object GoBack : ListFullAction()
    object TakeExam : ListFullAction()
    object AddNewWord : ListFullAction()
    data class SearchWord(val query: String) : ListFullAction()
    data class PressOnWord(val wordId: Long) : ListFullAction()
    data class InitialLoadData(val listId: Long) : ListFullAction()
    data class PlayAudio(
        val word: WordRV,
        val onStartListener: () -> Unit,
        val onCompletionListener: () -> Unit
    ) : ListFullAction()
}
