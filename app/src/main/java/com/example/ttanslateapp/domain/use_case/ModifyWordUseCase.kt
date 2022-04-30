package com.example.ttanslateapp.domain.use_case

import com.example.ttanslateapp.data.model.TranslatedWordDb
import com.example.ttanslateapp.domain.TranslatedWordRepository
import com.example.ttanslateapp.domain.model.edit.TranslateWordItem
import java.util.*
import javax.inject.Inject

class ModifyWordUseCase @Inject constructor(
    private val repository: TranslatedWordRepository
) {
    suspend operator fun invoke(word: String, description: String) {
//        val validateResults = ValidateTranslatedWord.validateInput()

        val newWord = TranslatedWordDb(
            id = 100,
            value = word,
            translations = listOf(TranslateWordItem(value = word, id = "100", createdAt = System.currentTimeMillis(), updatedAt =System.currentTimeMillis())),
            description = description,
            sound = null,
            langFrom = "UA",
            langTo = "EN",
            hintList = listOf(),
            answerList = listOf()
        )


        repository.modifyWord(newWord)
    }
}