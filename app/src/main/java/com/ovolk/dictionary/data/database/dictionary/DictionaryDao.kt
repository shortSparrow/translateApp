package com.ovolk.dictionary.data.database.dictionary

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.ovolk.dictionary.data.model.DictionaryDb
import com.ovolk.dictionary.util.DICTIONARIES
import kotlinx.coroutines.flow.Flow

@Dao
interface DictionaryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNewDictionary(dictionary: DictionaryDb): Long

    @Query("DELETE FROM DICTIONARIES WHERE id IN (:dictionariesIdList)")
    suspend fun deleteDictionaries(dictionariesIdList: List<Long>): Int

    @Query("SELECT * FROM DICTIONARIES")
    fun getDictionaryList(): Flow<List<DictionaryDb>>

    @Query("SELECT COUNT(*) FROM DICTIONARIES")
    suspend fun getDictionaryListSize(): Int

    @Query("SELECT * FROM DICTIONARIES WHERE id = :id")
    suspend fun getDictionary(id: Long): DictionaryDb?

    @Query("SELECT * FROM DICTIONARIES WHERE id = :id")
    fun getDictionaryFlow(id: Long): Flow<DictionaryDb?>

    @Query("SELECT * FROM DICTIONARIES WHERE lang_from_code = :langFromCode AND lang_to_code = :langToCode")
    suspend fun getDictionaryByLang(
        langFromCode: String,
        langToCode: String
    ): DictionaryDb?

    @Query("SELECT * FROM DICTIONARIES WHERE is_active=1 LIMIT 1")
    suspend fun getCurrentActiveDictionary(): DictionaryDb?

    @Query("SELECT * FROM DICTIONARIES WHERE is_active=1 LIMIT 1")
     fun getCurrentActiveDictionaryFlow(): Flow<DictionaryDb?>

    // affect all dictionaries which has is_active=true and replace value to is_active=false
    @Query("UPDATE $DICTIONARIES SET is_active=0 WHERE is_active = 1")
    suspend fun resetAllActiveDictionaries()

    // set dictionary is_active
    @Query("UPDATE $DICTIONARIES SET is_active=:isActive WHERE id=:id")
    suspend fun makeDictionaryActive(id: Long, isActive: Boolean): Int

    @Transaction
    suspend fun setDictionaryActive(id: Long, isActive: Boolean): Int {
        resetAllActiveDictionaries()
        return makeDictionaryActive(id, isActive)
    }
}