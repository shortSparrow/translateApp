package com.ovolk.dictionary.data.database.migration

import androidx.sqlite.db.SupportSQLiteDatabase
import com.ovolk.dictionary.util.DELAYED_UPDATE_WORDS_PRIORITY

fun migrateFrom4To5(database: SupportSQLiteDatabase) {
    database.execSQL(
        "CREATE TABLE IF NOT EXISTS $DELAYED_UPDATE_WORDS_PRIORITY (" +
                "'wordId' INTEGER PRIMARY KEY NOT NULL, " +
                "'priority' INTEGER NOT NULL)"
    )

}