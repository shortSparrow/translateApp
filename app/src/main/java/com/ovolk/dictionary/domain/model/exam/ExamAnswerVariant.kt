package com.ovolk.dictionary.domain.model.exam

data class ExamAnswerVariant(
    val id: Long = UNDEFINED_ID,
    val value: String
) {
    companion object {
        const val UNDEFINED_ID = 0L
    }
}