package com.example.ttanslateapp.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.ttanslateapp.data.model.ListItemDb
import com.example.ttanslateapp.util.TRANSLATED_WORDS_LISTS
import kotlinx.coroutines.flow.Flow

@Dao
interface ListsDao {
    @Query("SELECT * FROM $TRANSLATED_WORDS_LISTS")
    fun getAllLists(): Flow<List<ListItemDb>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNewList(newList: ListItemDb): Long

    @Query("UPDATE $TRANSLATED_WORDS_LISTS SET title=:title, updated_at=:updated_time WHERE id = :id")
    suspend fun renameList(title: String, id: Long, updated_time: Long = System.currentTimeMillis()): Int

    @Query("DELETE FROM $TRANSLATED_WORDS_LISTS WHERE id = :id")
    suspend fun deleteList(id: Long): Int
}