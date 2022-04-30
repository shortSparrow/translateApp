package com.example.ttanslateapp.domain

import androidx.lifecycle.LiveData
import com.example.ttanslateapp.data.model.TranslatedWordDb
import com.example.ttanslateapp.domain.model.WordRV

interface TranslatedWordRepository {
    fun getWordList(): LiveData<List<WordRV>>

    suspend fun getWordById(id: Long): WordRV

    suspend fun deleteWord(id: Long): Boolean

    suspend fun modifyWord(translatedWordDb: TranslatedWordDb): Boolean

    suspend fun findWord(query: String): LiveData<List<WordRV>>
}