package com.example.ttanslateapp.data.database

import android.app.Application
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.ttanslateapp.data.model.*
import com.example.ttanslateapp.util.TRANSLATED_WORDS_TABLE_NAME


@Database(
    version = 3,
    entities = [
        TranslateDb::class,
        HintDb::class,
        WordInfoDb::class,

        PotentialExamAnswerDb::class,
    ],
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun translatedWordDao(): TranslatedWordDao
    abstract fun examWordAnswerDao(): ExamWordAnswerDao

    companion object {
        private var INSTANCE: AppDatabase? = null
        private var LOCK = Any()
        private const val DB_NAME = "dictionary"

        private val migration_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE $TRANSLATED_WORDS_TABLE_NAME ADD COLUMN created_at INTEGER NOT NULL DEFAULT ${System.currentTimeMillis()}")
                database.execSQL("ALTER TABLE $TRANSLATED_WORDS_TABLE_NAME ADD COLUMN updated_at INTEGER NOT NULL DEFAULT ${System.currentTimeMillis()}")
            }
        }

        private val migration_2_3: Migration = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE 'word_translations' ('id' INTEGER, 'word_id' INTEGER, 'created_at' INTEGER, 'updated_at' INTEGER, 'value' STRING, 'is_hidden' BOOLEAN)")
                database.execSQL("CREATE TABLE 'word_hints' ('id' INTEGER, 'word_id' INTEGER, 'created_at' INTEGER, 'updated_at' INTEGER, 'value' STRING, 'is_hidden' BOOLEAN)")
            }
        }


        fun getInstance(application: Application): AppDatabase {
            INSTANCE?.let {
                return it
            }

            synchronized(LOCK) {
                INSTANCE?.let {
                    return it
                }

                val db = Room.databaseBuilder(
                    application,
                    AppDatabase::class.java,
                    DB_NAME
                ).fallbackToDestructiveMigration()
                    .addMigrations(migration_1_2)
                    .build()

                INSTANCE = db
                return db
            }
        }
    }
}
