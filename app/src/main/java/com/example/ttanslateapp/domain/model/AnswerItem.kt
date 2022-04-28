package com.example.ttanslateapp.domain.model

import androidx.room.Embedded
import java.util.*
import javax.inject.Inject

data class AnswerItem @Inject constructor(
    override val id: Long,
    override val createdAt: Date,
    override val updatedAt: Date,
    override val value: String
):Chips
