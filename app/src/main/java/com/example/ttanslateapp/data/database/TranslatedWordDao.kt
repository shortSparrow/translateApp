package com.example.ttanslateapp.data.database

import androidx.room.*
import com.example.ttanslateapp.data.model.*
import com.example.ttanslateapp.util.TRANSLATED_WORDS_TABLE_NAME
import kotlinx.coroutines.flow.Flow

@Dao
interface TranslatedWordDao {
    @Query("SELECT * FROM $TRANSLATED_WORDS_TABLE_NAME WHERE value LIKE :wordValue ORDER BY created_at DESC")
    fun searchWordList(wordValue: String): Flow<List<WordFullDb>>

    @Query("SELECT * FROM $TRANSLATED_WORDS_TABLE_NAME ORDER BY priority DESC LIMIT :count OFFSET :skip")
    suspend fun getExamWordList(count: Int, skip: Int): List<WordFullDb>

    @Query("SELECT COUNT(*) FROM $TRANSLATED_WORDS_TABLE_NAME")
    suspend fun getExamWordListSize(): Int

    @Query("SELECT * FROM $TRANSLATED_WORDS_TABLE_NAME ORDER BY priority DESC")
    suspend fun getExamWordList(): List<WordFullDb>

    @Query("SELECT * FROM $TRANSLATED_WORDS_TABLE_NAME WHERE id= :wordId")
    suspend fun getWordById(wordId: Long): WordFullDb

    @Query("DELETE FROM $TRANSLATED_WORDS_TABLE_NAME WHERE id = :wordId")
    suspend fun deleteWord(wordId: Long): Int

    // word parts
    @Query("UPDATE $TRANSLATED_WORDS_TABLE_NAME SET priority=:priority WHERE id = :id")
    suspend fun updatePriorityById(priority: Int, id: Long): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun modifyWordInfo(wordInfoDb: WordInfoDb): Long

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun modifyTranslates(translates: List<TranslateDb>): List<Long>

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun modifyHints(hints: List<HintDb>): List<Long>


//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun modifyWord(
//        wordInfoDb: WordInfoDb,
//        translates: List<TranslateDb>,
//        hints: List<HintDb>,
//    ): Long {
//        val modifyWordInfoId = modifyWordInfo(wordInfoDb)
//        val translatesResult = modifyTranslates(translates.map { it.copy(wordId = modifyWordInfoId) })
//        val hintsResult = modifyHints(hints.map { it.copy(wordId = modifyWordInfoId) })
//        return modifyWordInfoId
//    }

}