package com.ovolk.dictionary.domain.model.modify_word.modify_word_chip

interface Chip {
    val id: Long
    val localId: Long
    val createdAt: Long
    val updatedAt: Long
    val value: String
}