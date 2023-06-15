package com.ovolk.dictionary.data.model

import androidx.room.*
import com.ovolk.dictionary.domain.model.dictionary.Dictionary
import com.ovolk.dictionary.domain.model.modify_word.WordAudio
import com.ovolk.dictionary.util.TRANSLATED_WORDS_HINTS
import com.ovolk.dictionary.util.TRANSLATED_WORDS_TABLE_NAME
import com.ovolk.dictionary.util.TRANSLATED_WORDS_TRANSLATIONS
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

    @Relation(parentColumn = "dictionary_id", entityColumn = "id", entity = DictionaryDb::class)
    val dictionary: DictionaryDb,
)


@Entity(
    tableName = TRANSLATED_WORDS_TABLE_NAME,
    foreignKeys = [ForeignKey(
        entity = DictionaryDb::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("dictionary_id"),
        onDelete = ForeignKey.CASCADE,
    )],
)
@TypeConverters(SoundConvertor::class)
data class WordInfoDb @Inject constructor(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val priority: Int,
    val value: String,
    val description: String,
    val sound: WordAudio?, // english sound
    @ColumnInfo(name = "lang_from") val langFrom: String,
    @ColumnInfo(name = "lang_to") val langTo: String,
    val transcription: String,
    @ColumnInfo(name = "created_at") val createdAt: Long,
    @ColumnInfo(name = "updated_at") val updatedAt: Long,
    @ColumnInfo(name = "word_list_id") val wordListId: Long?,
    @ColumnInfo(name = "dictionary_id") val dictionaryId: Long,
)


@Entity(
    tableName = TRANSLATED_WORDS_TRANSLATIONS,
    foreignKeys = [ForeignKey(
        entity = WordInfoDb::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("word_id"),
        onDelete = ForeignKey.CASCADE,
    )],
)
data class TranslateDb @Inject constructor(
    @PrimaryKey(autoGenerate = true) val id: Long = DEFAULT_ID,
    @ColumnInfo(name = "created_at") val createdAt: Long,
    @ColumnInfo(name = "updated_at") val updatedAt: Long,
    val value: String,
    @ColumnInfo(name = "is_hidden") val isHidden: Boolean,
    @ColumnInfo(name = "word_id") val wordId: Long,
) {
    companion object {
        const val DEFAULT_ID = 0L
    }
}

@Entity(
    tableName = TRANSLATED_WORDS_HINTS,
    foreignKeys = [ForeignKey(
        entity = WordInfoDb::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("word_id"),
        onDelete = ForeignKey.CASCADE,
    )],
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
