package com.example.ttanslateapp.domain.model

import java.util.*
import javax.inject.Inject

data class HintItem @Inject constructor(
    val id: Long,
    val createdAt: Date,
    val updatedAt: Date,
    val value: String,
)
