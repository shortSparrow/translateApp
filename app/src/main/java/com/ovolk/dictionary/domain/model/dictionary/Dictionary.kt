package com.ovolk.dictionary.domain.model.dictionary

data class Dictionary(
    val id: Long,
    val title: String,
    val langFromCode: String,
    val langToCode: String,
    val isActive: Boolean,
    val isSelected: Boolean,
)