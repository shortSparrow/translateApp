package com.ovolk.dictionary.data.mapper

import com.ovolk.dictionary.data.model.HintDb
import com.ovolk.dictionary.data.model.PotentialExamAnswerDb
import com.ovolk.dictionary.data.model.TranslateDb
import com.ovolk.dictionary.data.model.UpdatePriority
import com.ovolk.dictionary.data.model.UpdatePriorityDb
import com.ovolk.dictionary.data.model.WordFullDb
import com.ovolk.dictionary.data.model.WordInfoDb
import com.ovolk.dictionary.domain.model.exam.ExamAnswerVariant
import com.ovolk.dictionary.domain.model.exam.ExamWord
import com.ovolk.dictionary.domain.model.exam.ExamWordStatus
import com.ovolk.dictionary.domain.model.modify_word.ModifyWord
import com.ovolk.dictionary.domain.model.modify_word.WordRV
import com.ovolk.dictionary.domain.model.modify_word.modify_word_chip.HintItem
import com.ovolk.dictionary.domain.model.modify_word.modify_word_chip.Translate
import javax.inject.Inject

class WordMapper @Inject constructor(
    private val dictionaryMapper: DictionaryMapper
) {
    fun wordListDbToWordList(dbWordList: List<WordFullDb>) = dbWordList.map {
        wordFullDbToWordRv(it)
    }

    fun translateDbToLocal(translate: TranslateDb): Translate =
        Translate(
            id = translate.id,
            localId = translate.id,
            createdAt = translate.createdAt,
            updatedAt = translate.updatedAt,
            value = translate.value,
            isHidden = translate.isHidden,
        )

    fun translateLocalToDb(
        translate: Translate,
        wordId: Long
    ): TranslateDb =
        TranslateDb(
            id = translate.id,
            createdAt = translate.createdAt,
            updatedAt = translate.updatedAt,
            value = translate.value,
            isHidden = translate.isHidden,
            wordId = wordId
        )

    fun hintLocalToDb(hint: HintItem, wordId: Long): HintDb =
        HintDb(
            createdAt = hint.createdAt,
            updatedAt = hint.updatedAt,
            value = hint.value,
            wordId = wordId
        )

    fun hintDbToLocal(hint: HintDb): HintItem =
        HintItem(
            id = hint.id,
            localId = hint.id,
            createdAt = hint.createdAt,
            updatedAt = hint.updatedAt,
            value = hint.value,
        )


    fun wordFullDbToWordRv(wordDb: WordFullDb): WordRV = WordRV(
        id = wordDb.wordInfo.id,
        value = wordDb.wordInfo.value,
        translates = wordDb.translates.map { translateDbToLocal(it) },
        description = wordDb.wordInfo.description,
        sound = wordDb.wordInfo.sound,
        langFrom = wordDb.dictionary.langFromCode,
        langTo = wordDb.dictionary.langToCode,
        transcription = wordDb.wordInfo.transcription,
    )

    fun wordFullDbToModifyWord(wordDb: WordFullDb): ModifyWord = ModifyWord(
        id = wordDb.wordInfo.id,
        priority = wordDb.wordInfo.priority,
        value = wordDb.wordInfo.value,
        translates = wordDb.translates.map { translateDbToLocal(it) },
        description = wordDb.wordInfo.description,
        sound = wordDb.wordInfo.sound,
        hints = wordDb.hints.map { hintDbToLocal(it) },
        transcription = wordDb.wordInfo.transcription,
        createdAt = wordDb.wordInfo.createdAt,
        updatedAt = wordDb.wordInfo.updatedAt,
        wordListId = wordDb.wordInfo.wordListId,
        dictionary = dictionaryMapper.dictionaryDbToDictionary(wordDb.dictionary)
    )

    fun wordFullDbToUpdatePriority(wordDb: WordFullDb): UpdatePriority = UpdatePriority(
        wordId = wordDb.wordInfo.id,
        priority = wordDb.wordInfo.priority,
    )

    fun modifyWordToWordInfoDb(modifyWord: ModifyWord) = WordInfoDb(
        id = modifyWord.id,
        priority = modifyWord.priority,
        value = modifyWord.value,
        description = modifyWord.description,
        sound = modifyWord.sound,
        transcription = modifyWord.transcription,
        createdAt = modifyWord.createdAt,
        updatedAt = modifyWord.updatedAt,
        wordListId = modifyWord.wordListId,
        dictionaryId = modifyWord.dictionary.id
    )


    fun updatePriorityToUpdatePriorityDb(updatePriority: UpdatePriority) = UpdatePriorityDb(
        wordId = updatePriority.wordId,
        priority = updatePriority.priority
    )

    fun updatePriorityDbToUpdatePriority(updatePriorityDb: UpdatePriorityDb) = UpdatePriority(
        wordId = updatePriorityDb.wordId,
        priority = updatePriorityDb.priority
    )
}
