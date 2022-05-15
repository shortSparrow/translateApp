package com.example.ttanslateapp.domain

import com.example.ttanslateapp.data.model.TranslatedWordDb
import com.example.ttanslateapp.domain.model.ModifyWord
import com.example.ttanslateapp.domain.model.WordRV
import com.example.ttanslateapp.domain.model.exam.ExamWord
import kotlinx.coroutines.flow.Flow

interface TranslatedWordRepository {
    suspend fun getWordList(): Flow<List<WordRV>>

    suspend fun getExamWordList(count: Int): List<ExamWord>

    suspend fun updatePriorityById(priority: Int, id: Long): Boolean

    suspend fun getWordById(id: Long): ModifyWord

    suspend fun deleteWord(id: Long): Boolean

    suspend fun modifyWord(translatedWordDb: TranslatedWordDb): Boolean

    suspend fun searchWordList(query: String): Flow<List<WordRV>>

}