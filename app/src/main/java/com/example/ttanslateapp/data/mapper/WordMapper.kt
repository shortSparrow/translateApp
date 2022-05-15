package com.example.ttanslateapp.data.mapper

import com.example.ttanslateapp.data.model.TranslatedWordDb
import com.example.ttanslateapp.domain.model.ModifyWord
import com.example.ttanslateapp.domain.model.WordRV
import com.example.ttanslateapp.domain.model.exam.ExamWord
import com.example.ttanslateapp.domain.model.exam.ExamWordStatus
import com.example.ttanslateapp.domain.model.modify_word_chip.HintItem
import com.example.ttanslateapp.domain.model.modify_word_chip.TranslateWordItem
import com.example.ttanslateapp.presentation.exam.AnswerResult
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
        transcription = wordDb.transcription
    )

    fun wordDbToModifyWord(wordDb: TranslatedWordDb): ModifyWord = ModifyWord(
        id = wordDb.id,
        priority = wordDb.priority,
        value = wordDb.value,
        translates = wordDb.translates,
        description = wordDb.description,
        sound = wordDb.sound,
        langFrom = wordDb.langFrom,
        langTo = wordDb.langTo,
        hints = wordDb.hints!!, // FIXME delete !!
        transcription = wordDb.transcription,
    )

    fun wordDbToExamWord(wordDb: TranslatedWordDb): ExamWord = ExamWord(
        id = wordDb.id,
        value = wordDb.value,
        translates = wordDb.translates,
        hints = wordDb.hints!!, // FIXME delete !!
        priority = wordDb.priority,
        status = ExamWordStatus.UNPROCESSED,
        answerVariants = listOf("Hello", "car", "kotlin") // FIXME change on real answer list
    )


    fun modifyWordToDbWord(modifyWord: ModifyWord) = TranslatedWordDb(
        id = modifyWord.id,
        priority = modifyWord.priority,
        value = modifyWord.value,
        translates = modifyWord.translates,
        description = modifyWord.description,
        sound = modifyWord.sound,
        langFrom = modifyWord.langFrom,
        langTo = modifyWord.langTo,
        hints = modifyWord.hints,
        transcription = modifyWord.transcription
    )

}
