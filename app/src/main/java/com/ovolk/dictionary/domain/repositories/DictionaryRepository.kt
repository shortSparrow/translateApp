package com.ovolk.dictionary.domain.repositories

import com.ovolk.dictionary.domain.model.dictionary.Dictionary
import kotlinx.coroutines.flow.Flow

interface DictionaryRepository {
    suspend fun addNewDictionary(dictionary: Dictionary): Long
    suspend fun deleteDictionaries(dictionariesIdList: List<Long>): Boolean
    fun getDictionaryList(): Flow<List<Dictionary>>
    suspend fun getCurrentActiveDictionary(): Dictionary?
    suspend fun getDictionaryListSize(): Int
    suspend fun getDictionary(dictionaryId: Long): Dictionary?
    suspend fun getDictionaryByLang(langCodeFrom: String, langCodeTo: String): Dictionary?
    suspend fun setDictionaryActive(dictionaryId: Long, isActive: Boolean): Boolean
}