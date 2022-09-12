package com.example.ttanslateapp.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.ttanslateapp.util.TRANSLATED_WORDS_LISTS

@Entity(tableName = TRANSLATED_WORDS_LISTS)
data class ListItemDb(
    @PrimaryKey(autoGenerate = true) val id: Long = DEFAULT_ID,
    val title: String,
    @ColumnInfo(name = "created_at") val createdAt: Long,
    @ColumnInfo(name = "updated_at") val updatedAt: Long,
) {
    companion object {
        const val DEFAULT_ID = 0L
    }
}