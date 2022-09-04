package com.example.ttanslateapp.domain.model.modify_word

data class ModifyWordListItem(
    val id: Long,
    val title: String = "",
    val count: Int = 0,
    val isSelected: Boolean = false,
)