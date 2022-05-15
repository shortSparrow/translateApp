package com.example.ttanslateapp.data.database

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.ttanslateapp.data.model.TranslatedWordDb


@Database(entities = [TranslatedWordDb::class], version = 5, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun translatedWordDao(): TranslatedWordDao

    companion object {
        private var INSTANCE: AppDatabase? = null
        private var LOCK = Any()
        private const val DB_NAME = "dictionary"

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
                    .build()

                INSTANCE = db
                return  db
            }
        }
    }
}