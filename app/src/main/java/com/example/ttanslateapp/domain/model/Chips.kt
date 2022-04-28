package com.example.ttanslateapp.domain.model

import java.util.*

interface Chips {
    val id: Long
    val createdAt: Date
    val updatedAt: Date
    val value: String
}