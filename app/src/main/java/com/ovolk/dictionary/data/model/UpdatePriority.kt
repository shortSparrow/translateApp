package com.ovolk.dictionary.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ovolk.dictionary.util.DELAYED_UPDATE_WORDS_PRIORITY

@Entity(tableName = DELAYED_UPDATE_WORDS_PRIORITY)
data class UpdatePriorityDb(
    @PrimaryKey val wordId: Long,
    val priority: Int,
)

data class UpdatePriority(
    val priority: Int,
    val wordId: Long,
)