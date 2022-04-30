package com.example.ttanslateapp.domain.model

import com.example.ttanslateapp.data.model.Sound
import com.example.ttanslateapp.domain.model.edit.HintItem
import com.example.ttanslateapp.domain.model.edit.TranslateWordItem
import javax.inject.Inject

// look embedded annotation & foreign key

// TODO delete @Inject  if it possible Maybe just delete us useless class
data class ModifyWord @Inject constructor(
    val id: Long = UNDEFINED_ID,
    val value: String,
    val translateWords: List<TranslateWordItem>,
    val description: String,
    val sound: Sound?, // english sound
    val langFrom: String,
    val langTo: String,
    val hintList: List<HintItem>?,
    val answerList: List<AnswerItem>?
) {
    companion object {
        const val UNDEFINED_ID = 0L
    }
}
