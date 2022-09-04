package com.example.ttanslateapp.presentation.list_full

sealed class ListFullAction {
    object GoBack : ListFullAction()
    object TakeExam : ListFullAction()
    object AddNewWord : ListFullAction()
    data class SearchWord(val query: String) : ListFullAction()
    data class PressOnWord(val wordId: Long) : ListFullAction()
}
