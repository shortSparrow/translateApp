package com.example.ttanslateapp.data.database.migration

import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.ttanslateapp.util.TRANSLATED_WORDS_LISTS
import com.example.ttanslateapp.util.TRANSLATED_WORDS_TABLE_NAME

fun migrateFrom3To4(database: SupportSQLiteDatabase) {
    database.execSQL("ALTER TABLE $TRANSLATED_WORDS_TABLE_NAME ADD COLUMN word_list_id INTEGER DEFAULT NULL")

    database.execSQL(
        "CREATE TABLE IF NOT EXISTS $TRANSLATED_WORDS_LISTS (" +
                "'id' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "'created_at' INTEGER NOT NULL, " +
                "'updated_at' INTEGER  NOT NULL, " +
                "'title' TEXT NOT NULL)"
    )

}