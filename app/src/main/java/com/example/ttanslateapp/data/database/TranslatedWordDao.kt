package com.example.ttanslateapp.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.ttanslateapp.data.model.TranslatedWordDb
import com.example.ttanslateapp.util.TRANSLATED_WORDS_TABLE_NAME
import kotlinx.coroutines.flow.Flow

@Dao
interface TranslatedWordDao {
    @Query("SELECT * FROM $TRANSLATED_WORDS_TABLE_NAME")
    fun getWordList(): Flow<List<TranslatedWordDb>>

    @Query("SELECT * FROM $TRANSLATED_WORDS_TABLE_NAME WHERE value LIKE :wordValue")
    fun searchWordList(wordValue: String): Flow<List<TranslatedWordDb>>

    @Query("SELECT * FROM $TRANSLATED_WORDS_TABLE_NAME ORDER BY priority DESC LIMIT :count")
    suspend fun getExamWordList(count: Int): List<TranslatedWordDb>

    @Query("UPDATE $TRANSLATED_WORDS_TABLE_NAME SET priority=:priority WHERE id = :id")
   suspend fun updatePriorityById(priority: Int, id: Long): Int

    @Query("SELECT * FROM $TRANSLATED_WORDS_TABLE_NAME WHERE id= :wordId")
    suspend fun getWordById(wordId: Long): TranslatedWordDb

    @Query("DELETE FROM $TRANSLATED_WORDS_TABLE_NAME WHERE id = :wordId")
    suspend fun deleteWord(wordId: Long): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun modifyWord(translatedWord: TranslatedWordDb): Long
}