package com.ovolk.dictionary.presentation.list_full

import com.ovolk.dictionary.domain.model.modify_word.WordRV

enum class LoadingState { IDLE, PENDING, SUCCESS, FAILED }

data class ListFullState(
    val listId: Long = -1L,
    val wordList: List<WordRV> = emptyList(),
    val noAnyWords: Boolean = false,
    val loadingStatusWordList: LoadingState = LoadingState.IDLE
)
