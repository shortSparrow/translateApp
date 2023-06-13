package com.ovolk.dictionary.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ovolk.dictionary.util.DICTIONARIES

@Entity(tableName = DICTIONARIES)
data class DictionaryDb(
    @PrimaryKey(autoGenerate = true) val id: Long = DEFAULT_ID,
    val title: String,
    @ColumnInfo(name = "lang_from_code") val langFromCode: String,
    @ColumnInfo(name = "lang_to_code") val langToCode: String,
    @ColumnInfo(name = "is_active") val isActive: Boolean,
) {
    companion object {
        const val DEFAULT_ID = 0L
    }
}
