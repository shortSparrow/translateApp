package com.example.ttanslateapp.data.model

import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import com.example.ttanslateapp.domain.model.WordAudio
import com.example.ttanslateapp.util.TRANSLATED_WORDS_HINTS
import com.example.ttanslateapp.util.TRANSLATED_WORDS_TABLE_NAME
import com.example.ttanslateapp.util.TRANSLATED_WORDS_TRANSLATIONS
import javax.inject.Inject


class SoundConvertor {
    @TypeConverter
    fun fromSound(sound: WordAudio?) = sound?.fileName //  if value exist set value, else null

    @TypeConverter
    fun toSound(value: String?) = value?.let {
        WordAudio(fileName = it)
    }
}

@TypeConverters(SoundConvertor::class)
data class WordFullDb @Inject constructor(
    @Embedded
    val wordInfo: WordInfoDb,

    @Relation(parentColumn = "id", entityColumn = "word_id", entity = TranslateDb::class)
    val translates: List<TranslateDb>,

    @Relation(parentColumn = "id", entityColumn = "word_id", entity = HintDb::class)
    val hints: List<HintDb>,
)


@Entity(tableName = TRANSLATED_WORDS_TABLE_NAME)
@TypeConverters(SoundConvertor::class)
data class WordInfoDb @Inject constructor(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val priority: Int,
    val value: String,
    val description: String,
    val sound: WordAudio?, // english sound
    val langFrom: String,
    val langTo: String,
    val transcription: String,
    @ColumnInfo(name = "created_at") val createdAt: Long,
    @ColumnInfo(name = "updated_at") val updatedAt: Long,
)


@Entity(
    tableName = TRANSLATED_WORDS_TRANSLATIONS, foreignKeys = arrayOf(
        ForeignKey(
            entity = WordInfoDb::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("word_id"),
            onDelete = CASCADE,
        ),
    ),
    indices = [Index("word_id")]
)
data class TranslateDb @Inject constructor(
    @PrimaryKey(autoGenerate = true)
    val id: Long = DEFAULT_ID,
    @ColumnInfo(name = "word_id") val wordId: Long,
    @ColumnInfo(name = "created_at") val createdAt: Long,
    @ColumnInfo(name = "updated_at") val updatedAt: Long,
    val value: String,
    @ColumnInfo(name = "is_hidden") val isHidden: Boolean
) {
    companion object {
        const val DEFAULT_ID = 0L
    }
}

@Entity(
    tableName = TRANSLATED_WORDS_HINTS, foreignKeys = arrayOf(
        ForeignKey(
            entity = WordInfoDb::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("word_id"),
            onDelete = CASCADE,
        ),
    ),
    indices = [Index("word_id")]
)
data class HintDb @Inject constructor(
    @PrimaryKey(autoGenerate = true)
    val id: Long = DEFAULT_ID,
    @ColumnInfo(name = "word_id") val wordId: Long,
    @ColumnInfo(name = "created_at") val createdAt: Long,
    @ColumnInfo(name = "updated_at") val updatedAt: Long,
    val value: String,
) {
    companion object {
        const val DEFAULT_ID = 0L
    }
}
