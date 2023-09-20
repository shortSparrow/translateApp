package com.ovolk.dictionary.data.database.migration

import androidx.sqlite.db.SupportSQLiteDatabase
import com.ovolk.dictionary.util.EXAM_WORD_ANSWERS_TABLE_NAME


fun migrateFrom6To7(database: SupportSQLiteDatabase) {
    database.execSQL("DROP TABLE $EXAM_WORD_ANSWERS_TABLE_NAME")
}