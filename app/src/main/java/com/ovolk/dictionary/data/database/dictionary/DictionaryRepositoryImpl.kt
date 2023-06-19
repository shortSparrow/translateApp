package com.ovolk.dictionary.data.database.dictionary

import com.ovolk.dictionary.data.mapper.DictionaryMapper
import com.ovolk.dictionary.domain.model.dictionary.Dictionary
import com.ovolk.dictionary.domain.repositories.DictionaryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DictionaryRepositoryImpl @Inject constructor(
    private val dictionaryDao: DictionaryDao,
    private val dictionaryMapper: DictionaryMapper
) : DictionaryRepository {
    override suspend fun addNewDictionary(dictionary: Dictionary): Long {
        return dictionaryDao.addNewDictionary(dictionaryMapper.dictionaryToDictionaryDb(dictionary = dictionary))
    }

    override suspend fun deleteDictionaries(dictionariesIdList: List<Long>): Boolean {
        val response = dictionaryDao.deleteDictionaries(dictionariesIdList)
        return response == OPERATION_SUCCESSFUL
    }

    override fun getDictionaryList(): Flow<List<Dictionary>> {
        return dictionaryDao.getDictionaryList()
            .map { list -> list.map { dictionaryMapper.dictionaryDbToDictionary(it) } }
    }

    override suspend fun getCurrentActiveDictionary(): Dictionary? {
        val response = dictionaryDao.getCurrentActiveDictionary() ?: return null
        return dictionaryMapper.dictionaryDbToDictionary(response)
    }

    override suspend fun getDictionaryListSize(): Int {
        return dictionaryDao.getDictionaryListSize()
    }

    override suspend fun getDictionary(dictionaryId: Long): Dictionary? {
        val response = dictionaryDao.getDictionary(dictionaryId)
        return if (response == null) null else dictionaryMapper.dictionaryDbToDictionary(response)
    }

    override suspend fun getDictionaryByLang(
        langFromCode: String,
        langToCode: String
    ): Dictionary? {
        val response =
            dictionaryDao.getDictionaryByLang(langToCode = langToCode, langFromCode = langFromCode)
        return if (response == null) null else dictionaryMapper.dictionaryDbToDictionary(response)
    }

    override suspend fun setDictionaryActive(dictionaryId: Long, isActive: Boolean): Boolean {
        val response = dictionaryDao.setDictionaryActive(id = dictionaryId, isActive = isActive)
        return response == OPERATION_SUCCESSFUL
    }

    companion object {
        const val OPERATION_SUCCESSFUL = 1
    }
}