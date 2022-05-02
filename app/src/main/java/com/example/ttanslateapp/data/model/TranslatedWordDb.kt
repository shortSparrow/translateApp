package com.example.ttanslateapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.ttanslateapp.domain.model.AnswerItem
import com.example.ttanslateapp.domain.model.edit.HintItem
import com.example.ttanslateapp.domain.model.edit.TranslateWordItem
import com.example.ttanslateapp.util.TABLE_NAME
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


class TranslationConverters {
    @TypeConverter
    fun fromList(translations: List<TranslateWordItem>): String {
        val gson = Gson()
        return gson.toJson(translations)
    }

    @TypeConverter
    fun fromString(translations: String): List<TranslateWordItem> {
        val listType: Type = object : TypeToken<List<TranslateWordItem>?>() {}.type
        return Gson().fromJson(translations, listType)
    }
}

class HintConverters {
    @TypeConverter
    fun fromList(hints: List<HintItem>?): String {
        val gson = Gson()
        return gson.toJson(hints)
    }

    @TypeConverter
    fun fromString(hints: String): List<HintItem>? {
        val listType: Type = object : TypeToken<List<HintItem>?>() {}.type
        return Gson().fromJson(hints, listType)
    }
}


class AnswerConverters {
    @TypeConverter
    fun fromList(answers: List<AnswerItem>?): String {
        val gson = Gson()
        return gson.toJson(answers)
    }

    @TypeConverter
    fun fromString(answers: String): List<AnswerItem>? {
        val listType: Type = object : TypeToken<List<AnswerItem>?>() {}.type
        return Gson().fromJson(answers, listType)
    }
}



class SoundConvertor {
    @TypeConverter
    fun fromSound(sound: Sound?) = sound?.path //  if value exist set value, else null

    @TypeConverter
    fun toSound(value: String?) = value?.let {
        object : Sound {
            override val path = it // if value exist set value, else null
        }
    }
}

@Entity(tableName = TABLE_NAME)
@TypeConverters(SoundConvertor::class, TranslationConverters::class, HintConverters::class, AnswerConverters::class)
data class TranslatedWordDb(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val value: String,
    val translates: List<TranslateWordItem>,
    val description: String,
    val sound: Sound?, // english sound
    val langFrom: String,
    val langTo: String,
    val hints: List<HintItem>?,
)
