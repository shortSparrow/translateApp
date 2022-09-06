package com.example.ttanslateapp.data.model

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

data class FullListItem(
    @PrimaryKey(autoGenerate = true) val id: Long = DEFAULT_ID,
    val title: String,
    @ColumnInfo(name = "created_at") val createdAt: Long,
    @ColumnInfo(name = "updated_at") val updatedAt: Long,
    val count: Int = 0,
) {
    companion object {
        const val DEFAULT_ID = 0L
    }
}