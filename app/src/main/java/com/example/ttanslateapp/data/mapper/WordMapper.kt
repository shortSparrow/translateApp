package com.example.ttanslateapp.data.mapper

import com.example.ttanslateapp.data.model.TranslatedWordDb
import com.example.ttanslateapp.domain.model.ModifyWord
import com.example.ttanslateapp.domain.model.WordRV
import javax.inject.Inject

class WordMapper @Inject constructor() {
    fun wordListDbToWordList(dbWordList: List<TranslatedWordDb>) = dbWordList.map {
        wordDbToWordRV(it)
    }

    private fun wordDbToWordRV(wordDb: TranslatedWordDb): WordRV = WordRV(
        id = wordDb.id,
        value = wordDb.value,
        translates = wordDb.translates,
        description = wordDb.description,
        sound = wordDb.sound,
        langFrom = wordDb.langFrom,
        langTo = wordDb.langTo,
    )

    fun wordDbToModifyWord(wordDb: TranslatedWordDb): ModifyWord = ModifyWord(
        id = wordDb.id,
        value = wordDb.value,
        translates = wordDb.translates,
        description = wordDb.description,
        sound = wordDb.sound,
        langFrom = wordDb.langFrom,
        langTo = wordDb.langTo,
        hints = wordDb.hints
    )

    fun modifyWordToDbWord(modifyWord: ModifyWord) = TranslatedWordDb(
        id = modifyWord.id,
        value = modifyWord.value,
        translates = modifyWord.translates,
        description = modifyWord.description,
        sound = modifyWord.sound,
        langFrom = modifyWord.langFrom,
        langTo = modifyWord.langTo,
        hints = modifyWord.hints,
    )
}
