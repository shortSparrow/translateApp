package com.example.ttanslateapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.ttanslateapp.domain.model.AnswerItem
import com.example.ttanslateapp.domain.model.HintItem
import com.example.ttanslateapp.domain.model.TranslationItem
import com.example.ttanslateapp.util.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class TranslatedWordDb(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val value: String,
    val translations: List<TranslationItem>,
    val description: String,
    val sound: Any?, // english sound
    val langFrom: String,
    val langTo: String,
    val hintList: List<HintItem?>,
    val answerList: List<AnswerItem?>
)
