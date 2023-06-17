package com.ovolk.dictionary.data.database.migration

import android.content.ContentValues
import android.content.SharedPreferences
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import androidx.core.database.getLongOrNull
import androidx.sqlite.db.SupportSQLiteDatabase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ovolk.dictionary.data.database.app_settings.AppSettingsMigration
import com.ovolk.dictionary.data.model.DictionaryDb
import com.ovolk.dictionary.domain.model.dictionary.Dictionary
import com.ovolk.dictionary.domain.model.exam_reminder.ReminderTime
import com.ovolk.dictionary.domain.model.select_languages.Language
import com.ovolk.dictionary.domain.use_case.exam_remibder.GetTimeReminder
import com.ovolk.dictionary.presentation.DictionaryApp
import com.ovolk.dictionary.util.DELAYED_UPDATE_WORDS_PRIORITY
import com.ovolk.dictionary.util.DICTIONARIES
import com.ovolk.dictionary.util.SETTINGS_PREFERENCES
import com.ovolk.dictionary.util.TRANSLATED_WORDS_TABLE_NAME
import com.ovolk.dictionary.util.USER_STATE_PREFERENCES
import java.lang.reflect.Type

data class Lang(
    val langCode: String
)

fun migrateFrom5To6(database: SupportSQLiteDatabase) {
    val context = DictionaryApp.applicationContext()
    val userSettingsPreferences: SharedPreferences =
        context.getSharedPreferences(
            USER_STATE_PREFERENCES,
            AppCompatActivity.MODE_PRIVATE
        )

    val gson = Gson()
    val langFrom = userSettingsPreferences.getString("LANG_FROM", "")
    val langTo = userSettingsPreferences.getString("LANG_TO", "")
    if (langFrom?.isNotEmpty() == true && langTo?.isNotEmpty() == true) {
        val languageListType: Type = object : TypeToken<List<Lang>?>() {}.type
        val listLangFrom: List<Lang> = gson.fromJson(langFrom, languageListType)
        val listLangTo: List<Lang> = gson.fromJson(langTo, languageListType)

        // create dictionaries table
        database.execSQL(
            "CREATE TABLE IF NOT EXISTS $DICTIONARIES (" +
                    "'id' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "'lang_from_code' TEXT NOT NULL, " +
                    "'lang_to_code' TEXT NOT NULL, " +
                    "'is_active' INTEGER NOT NULL, " +
                    "'title' TEXT NOT NULL )"
        )

        // fill dictionaries table
        listLangFrom.forEachIndexed { langFromItemIndex, langItemFrom ->
            listLangTo.forEachIndexed { langToItemIndex, langItemTo ->
                val contentValues = ContentValues()
                contentValues.put("lang_from_code", langItemFrom.langCode)
                contentValues.put("lang_to_code", langItemTo.langCode)
                contentValues.put("is_active", langFromItemIndex == 0 && langToItemIndex == 0)
                contentValues.put(
                    "title",
                    "${langItemFrom.langCode.uppercase()} - ${langItemTo.langCode.uppercase()}"
                )

                database.insert(
                    DICTIONARIES,
                    SQLiteDatabase.CONFLICT_REPLACE,
                    contentValues
                )
            }
        }
    }

    database.execSQL(
        "CREATE TABLE IF NOT EXISTS 'TRANSLATED_WORDS_TABLE_NAME_temp' (" +
                "'id' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "'priority' INTEGER NOT NULL, " +
                "'value' TEXT NOT NULL, " +
                "'description' TEXT NOT NULL, " +
                "'sound' TEXT, " +
                "'lang_from' TEXT NOT NULL, " + // TODO probably delete
                "'lang_to' TEXT NOT NULL, " + // TODO probably delete
                "'transcription' TEXT NOT NULL, " +
                "'created_at' INTEGER NOT NULL, " +
                "'updated_at' INTEGER  NOT NULL, " +
                "'word_list_id' INTEGER, " +
                "'dictionary_id' INTEGER NOT NULL, " +
                "FOREIGN KEY('dictionary_id') REFERENCES $DICTIONARIES('id') ON DELETE CASCADE ON UPDATE NO ACTION )"
    )


    // fill TRANSLATED_WORDS_TABLE_NAME_temp table
    val c: Cursor = database.query("SELECT * FROM $TRANSLATED_WORDS_TABLE_NAME", emptyArray())
    if (c.moveToFirst()) {
        do {
            val id = c.getLong(0)
            val priority = c.getInt(1)
            val value = c.getString(2)
            val description = c.getString(3)
            val sound = c.getString(4)
            val langFromValue = c.getString(5)
            val langToValue = c.getString(6)
            val transcription = c.getString(7)
            val createdAt = c.getLong(8)
            val updatedAt = c.getLong(9)
            val wordListId = c.getLongOrNull(10)

            var dictionaryId: Long? = null

            val dictionaryIdCursor: Cursor =
                database.query("SELECT id from $DICTIONARIES WHERE lang_from_code='${langFromValue}' AND lang_to_code='${langToValue}'")
            if (dictionaryIdCursor.moveToNext()) {
                do {
                    dictionaryId = dictionaryIdCursor.getLong(0)
                } while (dictionaryIdCursor.moveToNext())
            }

            val contentValues = ContentValues()
            contentValues.put("id", id)
            contentValues.put("priority", priority)
            contentValues.put("value", value)
            contentValues.put("description", description)
            contentValues.put("sound", sound)
            contentValues.put("lang_from", langFromValue) // TODO probably delete
            contentValues.put("lang_to", langToValue) // TODO probably delete
            contentValues.put("transcription", transcription)
            contentValues.put("created_at", createdAt)
            contentValues.put("updated_at", updatedAt)
            contentValues.put("word_list_id", wordListId)
            if (dictionaryId != null) {
                contentValues.put("dictionary_id", dictionaryId)
            } else {
                // This may never happens, but if it happens user must be not blocked, so create additional dictionary
                val dictionariesCursor: Cursor = database.query("SELECT id from $DICTIONARIES")
                val newDictionaryId = dictionariesCursor.count + 1
                val dictionaryContentValues = ContentValues()
                contentValues.put("lang_from_code", langFromValue)
                contentValues.put("lang_to_code", langToValue)
                contentValues.put("is_active", false)
                contentValues.put("id", newDictionaryId)
                contentValues.put(
                    "title",
                    "${langFromValue.uppercase()} - ${langToValue.uppercase()}"
                )

                database.insert(
                    DICTIONARIES,
                    SQLiteDatabase.CONFLICT_REPLACE,
                    dictionaryContentValues
                )

                contentValues.put(
                    "dictionary_id",
                    newDictionaryId
                )
            }

            database.insert(
                "TRANSLATED_WORDS_TABLE_NAME_temp",
                SQLiteDatabase.CONFLICT_REPLACE,
                contentValues
            )
        } while (c.moveToNext())
    }
    c.close()

    // Remove old table
    database.execSQL("DROP TABLE $TRANSLATED_WORDS_TABLE_NAME")
    // Change name of table to correct one
    database.execSQL("ALTER TABLE 'TRANSLATED_WORDS_TABLE_NAME_temp' RENAME TO $TRANSLATED_WORDS_TABLE_NAME")

    // delete shared preferences because this no needed more
    AppSettingsMigration(context).deleteOldSharedPreferences()
}