package com.ovolk.dictionary.domain.model.exam

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class ExamAnswerVariant(
    val id: Long = UNDEFINED_ID,
    val value: String,
    initialIsSelected: Boolean = false
) {
    var isSelected by mutableStateOf(initialIsSelected)

    companion object {
        const val UNDEFINED_ID = 0L
    }
}
