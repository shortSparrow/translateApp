package com.ovolk.dictionary.data.database

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.ovolk.dictionary.data.database.migration.migrateFrom1To2
import com.ovolk.dictionary.data.database.migration.migrateFrom2To3
import com.ovolk.dictionary.data.database.migration.migrateFrom3To4
import com.ovolk.dictionary.data.model.*

@Database(
    version = 4,
    entities = [
        WordInfoDb::class,
        TranslateDb::class,
        HintDb::class,
        PotentialExamAnswerDb::class,
        ListItemDb::class
    ],
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun translatedWordDao(): com.ovolk.dictionary.data.database.TranslatedWordDao
    abstract fun examWordAnswerDao(): com.ovolk.dictionary.data.database.ExamWordAnswerDao
    abstract fun listsDao(): com.ovolk.dictionary.data.database.ListsDao

    companion object {
        private var INSTANCE: com.ovolk.dictionary.data.database.AppDatabase? = null
        private var LOCK = Any()
        private const val DB_NAME = "dictionary"

        private val migration_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                migrateFrom1To2(database)
            }
        }

        private val migration_2_3: Migration = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                migrateFrom2To3(database)
            }
        }


        private val migration_3_4: Migration = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                migrateFrom3To4(database)
            }
        }


        fun getInstance(application: Application): com.ovolk.dictionary.data.database.AppDatabase {
            com.ovolk.dictionary.data.database.AppDatabase.Companion.INSTANCE?.let {
                return it
            }

            synchronized(com.ovolk.dictionary.data.database.AppDatabase.Companion.LOCK) {
                com.ovolk.dictionary.data.database.AppDatabase.Companion.INSTANCE?.let {
                    return it
                }

                val db = Room.databaseBuilder(
                    application,
                    com.ovolk.dictionary.data.database.AppDatabase::class.java,
                    com.ovolk.dictionary.data.database.AppDatabase.Companion.DB_NAME
                ).fallbackToDestructiveMigration()
                    .addMigrations(
                        com.ovolk.dictionary.data.database.AppDatabase.Companion.migration_1_2,
                        com.ovolk.dictionary.data.database.AppDatabase.Companion.migration_2_3,
                        com.ovolk.dictionary.data.database.AppDatabase.Companion.migration_3_4
                    )
                    .build()

                com.ovolk.dictionary.data.database.AppDatabase.Companion.INSTANCE = db
                return db
            }
        }
    }
}
