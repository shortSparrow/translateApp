package com.example.ttanslateapp.data.mapper

import com.example.ttanslateapp.data.model.Sound
import com.example.ttanslateapp.data.model.TranslatedWordDb
import com.example.ttanslateapp.domain.model.AnswerItem
import com.example.ttanslateapp.domain.model.ModifyWord
import com.example.ttanslateapp.domain.model.WordRV
import com.example.ttanslateapp.domain.model.edit.HintItem
import com.example.ttanslateapp.domain.model.edit.TranslateWordItem
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
        langFrom = wordDb.langFrom,
        langTo = wordDb.langTo,
    )

    fun modifyWordToDbWord(modifyWord: ModifyWord) = TranslatedWordDb(
        id = modifyWord.id,
        value = modifyWord.value,
        translations = modifyWord.translateWords,
        description = modifyWord.description,
        sound = modifyWord.sound,
        langFrom = modifyWord.langFrom,
        langTo = modifyWord.langTo,
        hintList = modifyWord.hintList,
        answerList = modifyWord.answerList,
    )
}
