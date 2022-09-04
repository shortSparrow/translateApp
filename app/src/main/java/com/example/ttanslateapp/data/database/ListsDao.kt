package com.example.ttanslateapp.data.database

import androidx.room.*
import com.example.ttanslateapp.data.model.ListItemDb
import com.example.ttanslateapp.util.TRANSLATED_WORDS_LISTS
import com.example.ttanslateapp.util.TRANSLATED_WORDS_TABLE_NAME
import kotlinx.coroutines.flow.Flow

@Dao
interface ListsDao {
    @Query(
        "SELECT word_lists.*, Count(translated_words.word_list_id) as count" +
                " FROM word_lists LEFT JOIN translated_words" +
                " ON word_lists.id = translated_words.word_list_id" +
                " GROUP BY word_lists.id"
    )
    fun getAllLists(): Flow<List<ListItemDb>>

    @Query(
        "SELECT word_lists.*, Count(translated_words.word_list_id) as count" +
                " FROM word_lists LEFT JOIN translated_words" +
                " ON word_lists.id = translated_words.word_list_id" +
                " WHERE word_lists.id =:id" +
                " GROUP BY word_lists.id"
    )
    fun getListById(id:Long): ListItemDb?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNewList(newList: ListItemDb): Long

    @Query("UPDATE $TRANSLATED_WORDS_LISTS SET title=:title, updated_at=:updated_time WHERE id = :id")
    suspend fun renameList(
        title: String,
        id: Long,
        updated_time: Long = System.currentTimeMillis()
    ): Int

    @Query("DELETE FROM $TRANSLATED_WORDS_LISTS WHERE id in (:idLists)")
    suspend fun deleteList(idLists: List<Long>)
}