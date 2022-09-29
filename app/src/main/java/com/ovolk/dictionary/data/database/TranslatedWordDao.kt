package com.ovolk.dictionary.data.database

import androidx.room.*
import com.ovolk.dictionary.data.mapper.WordMapper
import com.ovolk.dictionary.data.model.HintDb
import com.ovolk.dictionary.data.model.TranslateDb
import com.ovolk.dictionary.data.model.WordFullDb
import com.ovolk.dictionary.data.model.WordInfoDb
import com.ovolk.dictionary.domain.model.modify_word.ModifyWord
import com.ovolk.dictionary.util.TRANSLATED_WORDS_TABLE_NAME
import kotlinx.coroutines.flow.Flow

@Dao
interface TranslatedWordDao {
    @Query("SELECT * FROM $TRANSLATED_WORDS_TABLE_NAME WHERE value LIKE :query ORDER BY created_at DESC")
    fun searchWordList(query: String): Flow<List<WordFullDb>>

    @Query("SELECT COUNT(*) FROM $TRANSLATED_WORDS_TABLE_NAME")
    fun searchWordListSize(): Flow<Int>

    @Query("SELECT * FROM $TRANSLATED_WORDS_TABLE_NAME ORDER BY priority DESC, updated_at DESC LIMIT :count OFFSET :skip")
    suspend fun getExamWordList(count: Int, skip: Int): List<WordFullDb>

    @Query("SELECT * FROM $TRANSLATED_WORDS_TABLE_NAME WHERE word_list_id=:listId ORDER BY priority DESC, updated_at DESC LIMIT :count OFFSET :skip")
    suspend fun getExamWordListFromOneList(count: Int, skip: Int, listId: Long): List<WordFullDb>

    @Query("SELECT COUNT(*) FROM $TRANSLATED_WORDS_TABLE_NAME")
    suspend fun getExamWordListSize(): Int

    @Query("SELECT COUNT(*) FROM $TRANSLATED_WORDS_TABLE_NAME  WHERE word_list_id=:listId")
    suspend fun getExamWordListSizeForOneList(listId: Long): Int


    @Query("SELECT * FROM $TRANSLATED_WORDS_TABLE_NAME WHERE id= :wordId")
    suspend fun getWordById(wordId: Long): WordFullDb

    @Query("DELETE FROM $TRANSLATED_WORDS_TABLE_NAME WHERE id = :wordId")
    suspend fun deleteWord(wordId: Long): Int

    // word parts
    @Query("UPDATE $TRANSLATED_WORDS_TABLE_NAME SET priority=:priority, updated_at=:updated_time WHERE id = :id")
    suspend fun updatePriorityById(
        priority: Int,
        id: Long,
        updated_time: Long = System.currentTimeMillis()
    ): Int

    @Transaction
    suspend fun modifyWord(
        word: ModifyWord,
        mapper: WordMapper,
    ): Long {
        val wordId = modifyWordInfo(mapper.modifyWordToWordInfoDb(word))

        val translateList = word.translates.map {
            mapper.translateLocalToDb(
                translate = it,
                wordId = wordId
            )
        }

        val hintList = word.hints.map {
            mapper.hintLocalToDb(
                hint = it,
                wordId = wordId
            )
        }
        modifyTranslates(translateList)
        modifyHints(hintList)
        return wordId
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun modifyWordInfo(wordInfoDb: WordInfoDb): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun modifyTranslates(translates: List<TranslateDb>): List<Long>

    suspend fun addHiddenTranslateWithUpdatePriority(
        translates: List<TranslateDb>,
        priority: Int,
        wordId: Long
    ): List<Long> {
        updatePriorityById(id = wordId, priority = priority)
        return modifyTranslates(translates)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun modifyHints(hints: List<HintDb>): List<Long>

}