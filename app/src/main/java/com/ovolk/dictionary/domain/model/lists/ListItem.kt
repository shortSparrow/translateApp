package com.ovolk.dictionary.domain.model.lists

data class ListItem(
    val id: Long,
    val title: String = "",
    val count: Int = 0,
    val isSelected: Boolean = false,
    val createdAt: Long,
    val updatedAt: Long,
    val dictionaryId: Long,
)

data class ModifyWordListItem(
    val id: Long,
    val title: String = "",
    val count: Int = 0,
    val isSelected: Boolean = false,
    val dictionaryId: Long,
)