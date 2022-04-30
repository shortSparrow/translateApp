package com.example.ttanslateapp.domain.model

import java.util.*
import javax.inject.Inject

data class AnswerItem @Inject constructor(
    override val id: String,
    override val createdAt: Long,
    override val updatedAt: Long,
    override val value: String
):Chip
