package com.example.ttanslateapp.domain

import com.example.ttanslateapp.data.model.TranslatedWordDb
import com.example.ttanslateapp.domain.model.ModifyWord
import com.example.ttanslateapp.domain.model.WordRV
import kotlinx.coroutines.flow.Flow

interface TranslatedWordRepository {
    suspend fun getWordList(): Flow<List<WordRV>>

    suspend fun getWordById(id: Long): ModifyWord

    suspend fun deleteWord(id: Long): Boolean

    suspend fun modifyWord(translatedWordDb: TranslatedWordDb): Boolean

    suspend fun searchWordList(query: String): Flow<List<WordRV>>
}