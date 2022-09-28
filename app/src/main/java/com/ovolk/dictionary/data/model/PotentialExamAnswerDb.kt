package com.ovolk.dictionary.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ovolk.dictionary.util.EXAM_WORD_ANSWERS_TABLE_NAME

@Entity(tableName = EXAM_WORD_ANSWERS_TABLE_NAME)
data class PotentialExamAnswerDb(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val value: String
)