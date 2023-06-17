package com.ovolk.dictionary.data.database.exam

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ovolk.dictionary.data.model.PotentialExamAnswerDb
import com.ovolk.dictionary.util.EXAM_WORD_ANSWERS_TABLE_NAME

@Dao
interface ExamWordAnswerDao {
    @Query("SELECT * FROM $EXAM_WORD_ANSWERS_TABLE_NAME ORDER BY RANDOM() LIMIT :limit")
    suspend fun getWordAnswerList(limit: Int): List<PotentialExamAnswerDb>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun modifyWordAnswer(examAnswerVariant: PotentialExamAnswerDb): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setWordAnswerList(list: List<PotentialExamAnswerDb>)

}