package com.example.ttanslateapp.domain

import com.example.ttanslateapp.data.in_memory_storage.InMemoryStorage
import com.example.ttanslateapp.data.mapper.WordMapper
import com.example.ttanslateapp.data.model.HintDb
import com.example.ttanslateapp.data.model.TranslateDb
import com.example.ttanslateapp.data.model.WordInfoDb
import com.example.ttanslateapp.domain.model.modify_word.ModifyWord
import com.example.ttanslateapp.domain.model.modify_word.WordRV
import com.example.ttanslateapp.domain.model.exam.ExamWord
import kotlinx.coroutines.flow.Flow

interface TranslatedWordRepository : InMemoryStorage {
    suspend fun getExamWordList(count: Int, skip: Int): List<ExamWord>
    suspend fun getExamWordListFromOneList(count: Int, skip: Int, listId: Long): List<ExamWord>
    suspend fun getExamWordListSize(): Int
    suspend fun getExamWordListSizeForOneList(listId: Long):Int

    suspend fun searchWordList(query: String): Flow<List<WordRV>>
    suspend fun searchWordListSize(): Flow<Int>

    suspend fun getWordById(id: Long): ModifyWord

    suspend fun deleteWord(id: Long): Boolean

    suspend fun updatePriorityById(priority: Int, id: Long): Boolean
    suspend fun addHiddenTranslateWithUpdatePriority(
        translates: List<TranslateDb>,
        priority: Int,
        wordId: Long
    ): List<Long>
    suspend fun modifyWord(word: ModifyWord, mapper: WordMapper): Long
    suspend fun modifyWordTranslates(translates: List<TranslateDb>): List<Long>
    suspend fun modifyWordHints(hints: List<HintDb>): List<Long>
    suspend fun modifyWordInfo(wordInfoDb: WordInfoDb): Long

}