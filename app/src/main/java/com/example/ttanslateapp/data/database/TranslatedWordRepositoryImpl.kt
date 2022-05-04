package com.example.ttanslateapp.data.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.ttanslateapp.data.mapper.WordMapper
import com.example.ttanslateapp.data.model.TranslatedWordDb
import com.example.ttanslateapp.domain.TranslatedWordRepository
import com.example.ttanslateapp.domain.model.ModifyWord
import com.example.ttanslateapp.domain.model.WordRV
import timber.log.Timber
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
        return translatedWordDao.deleteWord(id) != WORD_IS_NOT_FOUND
    }

    override suspend fun modifyWord(translatedWordDb: TranslatedWordDb): Boolean {
        val modifiedWordId = translatedWordDao.modifyWord(translatedWordDb)
        return modifiedWordId.toInt() != WORD_IS_NOT_FOUND
    }

    override suspend fun searchWordList(query: String): LiveData<List<WordRV>> {
        return Transformations.map(translatedWordDao.searchWordList("%$query%")) {
            mapper.wordListDbToWordList(it)
        }
    }
    companion object {
        private const val WORD_IS_NOT_FOUND = -1
    }

}