package com.example.ttanslateapp.domain.model.edit

import com.example.ttanslateapp.domain.model.Chip
import java.util.*
import javax.inject.Inject

data class HintItem @Inject constructor(
    override val id: String,
    override val createdAt: Long,
    override val updatedAt: Long,
    override val value: String,
): Chip
