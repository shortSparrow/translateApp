package com.ovolk.dictionary.data.database

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.ovolk.dictionary.data.database.dictionary.DictionaryDao
import com.ovolk.dictionary.data.database.migration.migrateFrom1To2
import com.ovolk.dictionary.data.database.migration.migrateFrom2To3
import com.ovolk.dictionary.data.database.migration.migrateFrom3To4
import com.ovolk.dictionary.data.database.migration.migrateFrom4To5
import com.ovolk.dictionary.data.database.migration.migrateFrom5To6
import com.ovolk.dictionary.data.database.migration.migrateFrom6To7
import com.ovolk.dictionary.data.database.word_lists.ListsDao
import com.ovolk.dictionary.data.database.words.TranslatedWordDao
import com.ovolk.dictionary.data.model.DictionaryDb
import com.ovolk.dictionary.data.model.HintDb
import com.ovolk.dictionary.data.model.ListItemDb
import com.ovolk.dictionary.data.model.TranslateDb
import com.ovolk.dictionary.data.model.UpdatePriorityDb
import com.ovolk.dictionary.data.model.WordInfoDb

@Database(
    version = 7,
    entities = [
        WordInfoDb::class,
        TranslateDb::class,
        HintDb::class,
        ListItemDb::class,
        UpdatePriorityDb::class,
        DictionaryDb::class,
    ],
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun translatedWordDao(): TranslatedWordDao
    abstract fun dictionaryDaoDao(): DictionaryDao
    abstract fun listsDao(): ListsDao

    companion object {
        private var INSTANCE: AppDatabase? = null
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

        private val migration_4_5: Migration = object : Migration(4, 5) {
            override fun migrate(database: SupportSQLiteDatabase) {
                migrateFrom4To5(database)
            }
        }

        private val migration_5_6: Migration = object : Migration(5, 6) {
            override fun migrate(database: SupportSQLiteDatabase) {
                migrateFrom5To6(database)
            }
        }

        private val migration_6_7: Migration = object : Migration(6, 7) {
            override fun migrate(database: SupportSQLiteDatabase) {
                migrateFrom6To7(database)
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
                    .addMigrations(
                        migration_1_2,
                        migration_2_3,
                        migration_3_4,
                        migration_4_5,
                        migration_5_6,
                        migration_6_7
                    )
                    .build()

                INSTANCE = db
                return db
            }
        }
    }
}
