package com.example.ttanslateapp.domain.model

import java.util.*

data class TranslationItem(
    val id: Long,
    val createdAt: Date,
    val updatedAt: Date,
    val value: String,
)
