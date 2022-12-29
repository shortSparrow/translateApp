package com.ovolk.dictionary.di

import android.app.Application
import com.ovolk.dictionary.data.database.AppDatabase
import com.ovolk.dictionary.data.database.ExamWordAnswerDao
import com.ovolk.dictionary.data.database.ListsDao
import com.ovolk.dictionary.data.database.TranslatedWordDao
import com.ovolk.dictionary.data.in_memory_storage.InMemoryStorage
import com.ovolk.dictionary.data.in_memory_storage.LocalCache
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideListDao(context: Application): ListsDao {
        return AppDatabase.getInstance(context).listsDao()
    }

    @Provides
    @Singleton
    fun providesTranslatedWordDao(context: Application): TranslatedWordDao {
        return AppDatabase.getInstance(context).translatedWordDao()
    }

    @Provides
    @Singleton
    fun providesExamWordAnswerDao(context: Application): ExamWordAnswerDao {
        return AppDatabase.getInstance(context).examWordAnswerDao()
    }

    @Provides
    @Singleton
    fun providesInMemoryStorage(): InMemoryStorage = LocalCache()

}

