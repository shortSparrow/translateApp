package com.example.ttanslateapp.data.mapper

import com.example.ttanslateapp.data.model.TranslatedWordDb
import com.example.ttanslateapp.domain.model.WordRV
import javax.inject.Inject

class WordMapper @Inject constructor() {
    fun wordListDbToWordList(dbWordList: List<TranslatedWordDb>) = dbWordList.map {
        wordDbToWordRV(it)
    }

    fun wordDbToWordRV(wordDb: TranslatedWordDb): WordRV = WordRV(
        id = wordDb.id,
        value = wordDb.value,
        translations = wordDb.translations,
        description = wordDb.description,
        sound = wordDb.sound,
    )


}