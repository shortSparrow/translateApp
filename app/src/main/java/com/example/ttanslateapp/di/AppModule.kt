package com.example.ttanslateapp.di

import android.app.Application
import com.example.ttanslateapp.data.database.AppDatabase
import com.example.ttanslateapp.data.database.ExamWordAnswerDao
import com.example.ttanslateapp.data.database.ListsDao
import com.example.ttanslateapp.data.database.TranslatedWordDao
import com.example.ttanslateapp.data.in_memory_storage.InMemoryStorage
import com.example.ttanslateapp.data.in_memory_storage.LocalCache
import com.example.ttanslateapp.presentation.word_list.adapter.WordListAdapter
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

    @Provides
    @Singleton
    fun providesWordListAdapter(context: Application): WordListAdapter {
        return WordListAdapter(context)
    }
}

