package com.ovolk.dictionary.data.database.word_lists

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.ovolk.dictionary.data.model.FullListItem
import com.ovolk.dictionary.data.model.ListItemDb
import com.ovolk.dictionary.data.model.WordFullDb
import com.ovolk.dictionary.util.TRANSLATED_WORDS_LISTS
import com.ovolk.dictionary.util.TRANSLATED_WORDS_TABLE_NAME
import kotlinx.coroutines.flow.Flow

@Dao
interface ListsDao {
    @Query(
        "SELECT word_lists.*, Count(translated_words.word_list_id) as count" +
                " FROM word_lists LEFT JOIN translated_words " +
                " ON word_lists.id = translated_words.word_list_id WHERE word_lists.dictionary_id =:dictionaryId" +
                " GROUP BY word_lists.id"
    )
    fun getAllLists(dictionaryId: Long): Flow<List<FullListItem>>

    @Query(
        "SELECT word_lists.*, Count(translated_words.word_list_id) as count" +
                " FROM word_lists LEFT JOIN translated_words " +
                " ON word_lists.id = translated_words.word_list_id WHERE word_lists.dictionary_id =:dictionaryId" +
                " GROUP BY word_lists.id"
    )
    fun getAllListsForDictionary(dictionaryId: Long): List<FullListItem>

    @Query(
        "SELECT word_lists.*, Count(translated_words.word_list_id) as count " +
                "FROM word_lists LEFT JOIN translated_words " +
                "ON word_lists.id = translated_words.word_list_id " +
                "WHERE word_lists.id =:id " +
                "GROUP BY word_lists.id"
    )
    fun getListById(id: Long): FullListItem?

    @Query(
        "SELECT translated_words.* FROM translated_words " +
                "INNER JOIN word_lists ON translated_words.word_list_id=word_lists.id " +
                "WHERE translated_words.value LIKE :query AND word_lists.id=:listId " +
                "ORDER BY created_at DESC"
    )
    fun searchWordListByListId(query: String, listId: Long): Flow<List<WordFullDb>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNewList(newList: ListItemDb): Long

    @Query("UPDATE $TRANSLATED_WORDS_LISTS SET title=:title, updated_at=:updated_time WHERE id = :id")
    suspend fun renameList(
        title: String,
        id: Long,
        updated_time: Long = System.currentTimeMillis()
    ): Int


    @Query("DELETE FROM $TRANSLATED_WORDS_LISTS WHERE id in (:idLists)")
    suspend fun _deleteList(idLists: List<Long>)

    @Query("UPDATE $TRANSLATED_WORDS_TABLE_NAME SET word_list_id=null WHERE word_list_id in (:idLists)")
    suspend fun _removeListIdFromWords(idLists: List<Long>)

    @Transaction
    suspend fun deleteList(idLists: List<Long>) {
        _deleteList(idLists)
        _removeListIdFromWords(idLists)
    }

}