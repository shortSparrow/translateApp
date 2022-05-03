package com.example.ttanslateapp.data.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.ttanslateapp.data.mapper.WordMapper
import com.example.ttanslateapp.data.model.TranslatedWordDb
import com.example.ttanslateapp.domain.TranslatedWordRepository
import com.example.ttanslateapp.domain.model.ModifyWord
import com.example.ttanslateapp.domain.model.WordRV
import javax.inject.Inject

class TranslatedWordRepositoryImpl @Inject constructor(
    private val translatedWordDao: TranslatedWordDao,
    private val mapper: WordMapper
) : TranslatedWordRepository {
    override suspend fun getWordList(): LiveData<List<WordRV>> {
        return Transformations.map(translatedWordDao.getWordList()) {
            mapper.wordListDbToWordList(it)
        }
    }

    override suspend fun getWordById(id: Long): ModifyWord {
        return mapper.wordDbToModifyWord(translatedWordDao.getWordById(id))
    }

    override suspend fun deleteWord(id: Long): Boolean {
        return translatedWordDao.deleteWord(id) != -1
    }

    // FIXME Boolean -> add compare comparison with returned ID
    override suspend fun modifyWord(translatedWordDb: TranslatedWordDb): Boolean {
        translatedWordDao.modifyWord(translatedWordDb)
        return true
    }

    override suspend fun searchWordList(query: String): LiveData<List<WordRV>> {
        return Transformations.map(translatedWordDao.searchWordList("%$query%")) {
            mapper.wordListDbToWordList(it)
        }
    }
}