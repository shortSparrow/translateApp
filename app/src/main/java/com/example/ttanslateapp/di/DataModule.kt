package com.example.ttanslateapp.di

import android.app.Application
import com.example.ttanslateapp.data.database.*
import com.example.ttanslateapp.data.in_memory_storage.InMemoryStorage
import com.example.ttanslateapp.data.in_memory_storage.LocalCache
import com.example.ttanslateapp.domain.ExamWordAnswerRepository
import com.example.ttanslateapp.domain.TranslatedWordRepository
import com.example.ttanslateapp.presentation.word_list.adapter.WordListAdapter
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Singleton
    @Binds
    fun bindTranslatedWordRepository(impl: TranslatedWordRepositoryImpl): TranslatedWordRepository

    @Singleton
    @Binds
    fun bindExamWordAnswerRepository(impl: ExamWordAnswerRepositoryImpl): ExamWordAnswerRepository

    companion object {
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
        fun providesInMemoryStorage(): InMemoryStorage = LocalCache()

        @Provides
        @Singleton
        fun providesWordListAdapter(context: Application): WordListAdapter {
            return WordListAdapter(context)
        }
    }
}