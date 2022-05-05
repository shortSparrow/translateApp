package com.example.ttanslateapp.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.ttanslateapp.data.model.TranslatedWordDb
import com.example.ttanslateapp.util.TABLE_NAME
import kotlinx.coroutines.flow.Flow

@Dao
interface TranslatedWordDao {
    @Query("SELECT * FROM $TABLE_NAME")
    fun getWordList(): Flow<List<TranslatedWordDb>>

    @Query("SELECT * FROM $TABLE_NAME WHERE value LIKE :wordValue")
    fun searchWordList(wordValue: String): Flow<List<TranslatedWordDb>>

    @Query("SELECT * FROM $TABLE_NAME WHERE id= :wordId")
    suspend fun getWordById(wordId: Long): TranslatedWordDb

    @Query("DELETE FROM $TABLE_NAME WHERE id = :wordId")
    suspend fun deleteWord(wordId: Long): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun modifyWord(translatedWord: TranslatedWordDb): Long
}