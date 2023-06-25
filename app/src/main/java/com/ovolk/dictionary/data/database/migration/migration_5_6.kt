package com.ovolk.dictionary.data.database.migration

import android.content.ContentValues
import android.content.Context
import android.content.SharedPreferences
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import androidx.core.database.getLongOrNull
import androidx.sqlite.db.SupportSQLiteDatabase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ovolk.dictionary.data.database.app_settings.AppSettingsMigration
import com.ovolk.dictionary.presentation.DictionaryApp
import com.ovolk.dictionary.util.DICTIONARIES
import com.ovolk.dictionary.util.TRANSLATED_WORDS_LISTS
import com.ovolk.dictionary.util.TRANSLATED_WORDS_TABLE_NAME
import com.ovolk.dictionary.util.USER_STATE_PREFERENCES
import java.lang.reflect.Type

data class Lang(
    val langCode: String,
)

data class DictionaryLang(
    val langFrom: String,
    val langTo: String,
)

private fun createDictionaryTable(database: SupportSQLiteDatabase, context: Context) {
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

        // get data for decides which dictionary must be active
        val dictionaryLang = mutableListOf<DictionaryLang>()
        val latestCreatedWordWithList =
            database.query("SELECT * from $TRANSLATED_WORDS_TABLE_NAME WHERE word_list_id IS NOT NULL ORDER BY created_at DESC LIMIT 1")
        latestCreatedWordWithList.moveToFirst()
        val isLatestCreatedWordWithListExist = latestCreatedWordWithList.count != 0



        // filter unused languages and fill dictionaryLang list
        listLangFrom.forEach{  langItemFrom ->
            listLangTo.forEach {  langItemTo ->
                val wordWithLanguagesCursor =
                    database.query("SELECT * FROM $TRANSLATED_WORDS_TABLE_NAME WHERE lang_from='${langItemFrom.langCode}' AND lang_to='${langItemTo.langCode}' LIMIT 1")
                if (wordWithLanguagesCursor.count == 0) return@forEach
                dictionaryLang.add(
                    DictionaryLang(
                        langTo = langItemTo.langCode,
                        langFrom = langItemFrom.langCode
                    )
                )
            }
        }

        // fill DICTIONARIES table
        dictionaryLang.forEachIndexed { index, dictionaryLanguages ->
           val isActiveDictionary = if(isLatestCreatedWordWithListExist) {
               val langFromValue = latestCreatedWordWithList.getString(5)
               val langToValue = latestCreatedWordWithList.getString(6)
               langFromValue == dictionaryLanguages.langFrom && langToValue == dictionaryLanguages.langTo
           } else {
               index == 0
           }

            val contentValues = ContentValues()
            contentValues.put("lang_from_code", dictionaryLanguages.langFrom)
            contentValues.put("lang_to_code", dictionaryLanguages.langTo)
            contentValues.put(
                "is_active",
                isActiveDictionary
            )

            contentValues.put(
                "title",
                "${dictionaryLanguages.langFrom.uppercase()} - ${dictionaryLanguages.langTo.uppercase()}"
            )

            database.insert(
                DICTIONARIES,
                SQLiteDatabase.CONFLICT_REPLACE,
                contentValues
            )
        }
    }
}

private fun createAndFillNewTranslatedWordTable(database: SupportSQLiteDatabase) {
    // create TRANSLATED_WORDS_TABLE_NAME_temp table
    database.execSQL(
        "CREATE TABLE IF NOT EXISTS 'TRANSLATED_WORDS_TABLE_NAME_temp' (" +
                "'id' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "'priority' INTEGER NOT NULL, " +
                "'value' TEXT NOT NULL, " +
                "'description' TEXT NOT NULL, " +
                "'sound' TEXT, " +
                "'transcription' TEXT NOT NULL, " +
                "'created_at' INTEGER NOT NULL, " +
                "'updated_at' INTEGER  NOT NULL, " +
                "'word_list_id' INTEGER, " +
                "'dictionary_id' INTEGER NOT NULL, " +
                "FOREIGN KEY('dictionary_id') REFERENCES $DICTIONARIES('id') ON DELETE CASCADE ON UPDATE NO ACTION )"
    )


    // fill TRANSLATED_WORDS_TABLE_NAME_temp table
    val translatedWordCursor: Cursor =
        database.query("SELECT * FROM $TRANSLATED_WORDS_TABLE_NAME", emptyArray())
    if (translatedWordCursor.moveToFirst()) {
        do {
            val id = translatedWordCursor.getLong(0)
            val priority = translatedWordCursor.getInt(1)
            val value = translatedWordCursor.getString(2)
            val description = translatedWordCursor.getString(3)
            val sound = translatedWordCursor.getString(4)
            val langFromValue = translatedWordCursor.getString(5)
            val langToValue = translatedWordCursor.getString(6)
            val transcription = translatedWordCursor.getString(7)
            val createdAt = translatedWordCursor.getLong(8)
            val updatedAt = translatedWordCursor.getLong(9)
            val wordListId = translatedWordCursor.getLongOrNull(10)

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
        } while (translatedWordCursor.moveToNext())
    }
    translatedWordCursor.close()
}

private fun createAndFillNewListTable(database: SupportSQLiteDatabase) {
    database.execSQL(
        "CREATE TABLE IF NOT EXISTS 'TRANSLATED_WORDS_LISTS_temp' (" +
                "'id' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "'title' TEXT NOT NULL, " +
                "'created_at' INTEGER NOT NULL, " +
                "'updated_at' INTEGER  NOT NULL, " +
                "'dictionary_id' INTEGER NOT NULL, " +
                "FOREIGN KEY('dictionary_id') REFERENCES $DICTIONARIES('id') ON DELETE CASCADE ON UPDATE NO ACTION )"
    )

    val listCursor: Cursor = database.query("SELECT * FROM $TRANSLATED_WORDS_LISTS", emptyArray())
    if (listCursor.moveToFirst()) {
        do {
            val id = listCursor.getLong(0)
            val title = listCursor.getString(1)
            val createdAt = listCursor.getLong(2)
            val updatedAt = listCursor.getLong(3)

            val wordsWithList: Cursor =
                database.query("SELECT * FROM $TRANSLATED_WORDS_TABLE_NAME WHERE word_list_id=${id}")
            // if lists if empty move it to active dictionary
            if (wordsWithList.count == 0) {
                val activeDictionaryCursor: Cursor =
                    database.query("SELECT id FROM $DICTIONARIES WHERE is_active=1 LIMIT 1")

                if (activeDictionaryCursor.moveToFirst()) {
                    val listContentValues = ContentValues()
                    val activeDictionaryId = activeDictionaryCursor.getLong(0)
                    listContentValues.put("id", id)
                    listContentValues.put("title", title)
                    listContentValues.put("created_at", createdAt)
                    listContentValues.put("updated_at", updatedAt)
                    listContentValues.put("dictionary_id", activeDictionaryId)

                    database.insert(
                        "TRANSLATED_WORDS_LISTS_temp",
                        SQLiteDatabase.CONFLICT_REPLACE,
                        listContentValues
                    )
                }
                activeDictionaryCursor.close()
            }

            if (wordsWithList.moveToFirst()) {
                do {
                    val langFromValue = wordsWithList.getString(5)
                    val langToValue = wordsWithList.getString(6)

                    // find dictionary with this languages
                    val dictionaryWithLanguages: Cursor =
                        database.query("SELECT * FROM $DICTIONARIES WHERE lang_from_code='${langFromValue}' AND lang_to_code='${langToValue}' LIMIT 1")

                    if (dictionaryWithLanguages.moveToFirst()) {
                        val listContentValues = ContentValues()
                        val dictionaryId = dictionaryWithLanguages.getLong(0)

                        listContentValues.put("id", id)
                        listContentValues.put("title", title)
                        listContentValues.put("created_at", createdAt)
                        listContentValues.put("updated_at", updatedAt)
                        listContentValues.put("dictionary_id", dictionaryId)

                        database.insert(
                            "TRANSLATED_WORDS_LISTS_temp",
                            SQLiteDatabase.CONFLICT_REPLACE,
                            listContentValues
                        )
                    }

                    dictionaryWithLanguages.close()

                } while (wordsWithList.moveToNext())
            }
            wordsWithList.close()

        } while (listCursor.moveToNext())
    }
    listCursor.close()

    // Remove old TRANSLATED_WORDS_LISTS table
    database.execSQL("DROP TABLE $TRANSLATED_WORDS_LISTS")
    // Change name of table to correct one
    database.execSQL("ALTER TABLE 'TRANSLATED_WORDS_LISTS_temp' RENAME TO $TRANSLATED_WORDS_LISTS")
}

fun migrateFrom5To6(database: SupportSQLiteDatabase) {
    val context = DictionaryApp.applicationContext()

    createDictionaryTable(database = database, context = context)
    createAndFillNewTranslatedWordTable(database)
    createAndFillNewListTable(database)


    // Remove old TRANSLATED_WORDS_TABLE_NAME table
    // (I do it after createAndFillNewListTable, because I need langFromValue and langToValue from old version TRANSLATED_WORDS_TABLE_NAME, in new version this fields are deleted)
    database.execSQL("DROP TABLE $TRANSLATED_WORDS_TABLE_NAME")
    // Change name of table to correct one
    database.execSQL("ALTER TABLE 'TRANSLATED_WORDS_TABLE_NAME_temp' RENAME TO $TRANSLATED_WORDS_TABLE_NAME")


    // delete shared preferences because this no needed more
    AppSettingsMigration(context).deleteOldSharedPreferences()
}