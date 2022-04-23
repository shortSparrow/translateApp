package com.example.ttanslateapp.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.ttanslateapp.data.mapper.WordMapper
import com.example.ttanslateapp.data.model.TranslatedWordDb
import com.example.ttanslateapp.domain.TranslatedWordRepository
import com.example.ttanslateapp.domain.model.TranslatedWord
import com.example.ttanslateapp.domain.model.WordRV

class TranslatedWordRepositoryImpl(
    private val translatedWordDao: TranslatedWordDao,
    private val mapper: WordMapper
) : TranslatedWordRepository {
    override suspend fun getWordList(): LiveData<List<WordRV>> {
        return Transformations.map(translatedWordDao.getWordList()){
            mapper.wordListDbToWordList(it)
        }
    }

    override suspend fun getWordById(id: Long): WordRV {
        return mapper.wordDbToWordRV(translatedWordDao.getWordById(id))
    }

    override suspend fun deleteWord(id: Long): Boolean {
        translatedWordDao.deleteWord(id)

        // FIXME Boolean -> true if success, and false is fail
        return true
    }

    override suspend fun modifyWord(translatedWordDb: TranslatedWordDb): Boolean {
        translatedWordDao.modifyWord(translatedWordDb)

        // FIXME Boolean -> true if success, and false is fail
        return true
    }

    override suspend fun findWord(query: String): LiveData<List<WordRV>> {
        return Transformations.map(translatedWordDao.getWordListBySearch(query)){
            mapper.wordListDbToWordList(it)
        }
    }

}