package com.ovolk.dictionary.presentation.dictionary_words

import androidx.compose.runtime.mutableStateListOf
import com.ovolk.dictionary.domain.LoadingState
import com.ovolk.dictionary.domain.model.dictionary.Dictionary
import com.ovolk.dictionary.domain.model.modify_word.WordRV

data class DictionaryWordsState(
    val searchValue: String = "",
    val dictionary: Dictionary? = null,
    val loadingStatus: LoadingState=LoadingState.IDLE,
    val totalWordListSize: Int = 0,
    val filteredWordList: List<WordRV> = mutableStateListOf(),
    val isDeleteConfirmModalOpen: Boolean = false,
)

sealed interface DictionaryWordsAction {
    data class OnSearchWord(val value: String) : DictionaryWordsAction
    data class HandleDeleteDictionaryModal(val isShown: Boolean) : DictionaryWordsAction
    data class OnPressWordItem(val wordId: Long) : DictionaryWordsAction
    data class PlayAudio(
        val word: WordRV,
        val onStartListener: () -> Unit,
        val onCompletionListener: () -> Unit
    ) : DictionaryWordsAction
    object OnPressTakeExam: DictionaryWordsAction
    object OnPressConfirmDelete: DictionaryWordsAction
    object OnPressEditDictionary: DictionaryWordsAction
    object OnPressAddNewWord : DictionaryWordsAction
    object MarkAsActive : DictionaryWordsAction
}