package com.ovolk.dictionary.data.model

import androidx.room.Embedded
import androidx.room.Relation

data class FullListItem(
    @Embedded
    val listInfo: ListItemDb,

    @Relation(parentColumn = "dictionary_id", entityColumn = "id", entity = DictionaryDb::class)
    val dictionary: DictionaryDb,

    val count: Int = 0,
)