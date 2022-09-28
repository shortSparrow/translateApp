package com.ovolk.dictionary.domain.model.lists

data class ListItem(
    val id: Long,
    val title: String = "",
    val count: Int = 0,
    val isSelected: Boolean = false,
    val createdAt: Long = 0L, // TODO delete default value
    val updatedAt: Long = 0L, // // TODO delete default value
)
