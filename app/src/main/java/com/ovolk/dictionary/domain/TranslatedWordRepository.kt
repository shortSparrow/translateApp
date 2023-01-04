package com.ovolk.dictionary.domain

import com.ovolk.dictionary.data.in_memory_storage.InMemoryStorage
import com.ovolk.dictionary.data.mapper.WordMapper
import com.ovolk.dictionary.data.model.*
import com.ovolk.dictionary.domain.model.exam.ExamWord
import com.ovolk.dictionary.domain.model.modify_word.ModifyWord
import com.ovolk.dictionary.domain.model.modify_word.WordRV
import kotlinx.coroutines.flow.Flow

interface TranslatedWordRepository : InMemoryStorage {
    suspend fun getExamWordList(count: Int, skip: Int): List<ExamWord>
    suspend fun getExamWordListFromOneList(count: Int, skip: Int, listId: Long): List<ExamWord>
    suspend fun getExamWordListSize(): Int
    suspend fun getExamWordListSizeForOneList(listId: Long): Int

    suspend fun searchWordList(query: String): Flow<List<WordRV>>
    suspend fun searchExactWordList(query: String): WordRV?
    suspend fun searchWordListSize(): Flow<Int>

    suspend fun getWordById(id: Long): ModifyWord

    suspend fun deleteWord(id: Long): Boolean

    suspend fun getWordsForDelayedUpdatePriority(): List<UpdatePriority>
    suspend fun addWordForDelayedUpdatePriority(word: UpdatePriorityDb): Boolean
    suspend fun updatePriorityById(priority: Int, id: Long): Boolean
    suspend fun updateWordsPriority(words: List<UpdatePriorityDb>)
    suspend fun addHiddenTranslateWithUpdatePriority(
        translates: List<TranslateDb>,
        priority: Int,
        wordId: Long
    ): List<Long>

    suspend fun modifyWord(word: ModifyWord, mapper: WordMapper): Long
    suspend fun modifyWordTranslates(translates: List<TranslateDb>): List<Long>
    suspend fun modifyWordHints(hints: List<HintDb>): List<Long>
    suspend fun modifyWordInfo(wordInfoDb: WordInfoDb): Long

    // update words which not updated a lot of time, and hase priority less than default
    suspend fun getWordsForSilentUpdatePriority(beforeUpdatedAt: Long, count: Int): List<UpdatePriority>
}