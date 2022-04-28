package com.example.ttanslateapp.domain.model.edit

import com.example.ttanslateapp.domain.model.Chips
import java.util.*
import javax.inject.Inject

data class HintItem @Inject constructor(
    override val id: Long,
    override val createdAt: Date,
    override val updatedAt: Date,
    override val value: String,
): Chips
