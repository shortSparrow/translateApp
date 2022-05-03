package com.example.ttanslateapp.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.ttanslateapp.data.model.TranslatedWordDb
import com.example.ttanslateapp.util.TABLE_NAME

@Dao
interface TranslatedWordDao {
    @Query("SELECT * FROM $TABLE_NAME")
    fun getWordList(): LiveData<List<TranslatedWordDb>>

    @Query("SELECT * FROM $TABLE_NAME WHERE value LIKE :wordValue")
    fun searchWordList(wordValue: String): LiveData<List<TranslatedWordDb>>

    @Query("SELECT * FROM $TABLE_NAME WHERE id= :wordId")
    suspend fun getWordById(wordId: Long): TranslatedWordDb

    @Query("DELETE FROM $TABLE_NAME WHERE id = :wordId")
    suspend fun deleteWord(wordId: Long): Int

    // TODO: add returning type as Long
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun modifyWord(translatedWord: TranslatedWordDb)
}