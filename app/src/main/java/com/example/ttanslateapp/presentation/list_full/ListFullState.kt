package com.example.ttanslateapp.presentation.list_full

import com.example.ttanslateapp.domain.model.modify_word.WordRV

data class ListFullState(
    val listId: Long = -1L,
    val wordList: List<WordRV> = emptyList()
)
