package com.example.ttanslateapp.domain

import com.example.ttanslateapp.data.model.HintDb
import com.example.ttanslateapp.data.model.TranslateDb
import com.example.ttanslateapp.data.model.WordInfoDb
import com.example.ttanslateapp.domain.model.ModifyWord
import com.example.ttanslateapp.domain.model.WordRV
import com.example.ttanslateapp.domain.model.exam.ExamWord
import kotlinx.coroutines.flow.Flow

interface TranslatedWordRepository {

    suspend fun getExamWordList(count: Int, skip: Int): List<ExamWord>
    suspend fun getExamWordListSize():Int

    suspend fun searchWordList(query: String): Flow<List<WordRV>>

    suspend fun getWordById(id: Long): ModifyWord

    suspend fun deleteWord(id: Long): Boolean


    suspend fun updatePriorityById(priority: Int, id: Long): Boolean
    suspend fun modifyWordTranslates(translates: List<TranslateDb>): List<Long>
    suspend fun modifyWordHints(hints: List<HintDb>): List<Long>
    suspend fun modifyWordInfo(wordInfoDb: WordInfoDb): Long

//    suspend fun modifyWord(
//        wordInfoDb: WordInfoDb,
//        translates: List<TranslateDb>,
//        hints: List<HintDb>,
//    ): Long
}