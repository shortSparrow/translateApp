package com.ovolk.dictionary.di

import com.ovolk.dictionary.data.database.app_settings.AppSettingsRepositoryImpl
import com.ovolk.dictionary.data.database.exam.ExamWordAnswerRepositoryImpl
import com.ovolk.dictionary.data.database.word_lists.ListsRepositoryImpl
import com.ovolk.dictionary.data.database.words.TranslatedWordRepositoryImpl
import com.ovolk.dictionary.data.database.dictionary.DictionaryRepositoryImpl
import com.ovolk.dictionary.domain.repositories.AppSettingsRepository
import com.ovolk.dictionary.domain.repositories.ExamWordAnswerRepository
import com.ovolk.dictionary.domain.repositories.TranslatedWordRepository
import com.ovolk.dictionary.domain.repositories.DictionaryRepository
import com.ovolk.dictionary.domain.repositories.ListsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {
    @Binds
    @Singleton
    fun bindListsRepository(impl: ListsRepositoryImpl): ListsRepository

    @Singleton
    @Binds
    fun bindTranslatedWordRepository(impl: TranslatedWordRepositoryImpl): TranslatedWordRepository

    @Singleton
    @Binds
    fun bindExamWordAnswerRepository(impl: ExamWordAnswerRepositoryImpl): ExamWordAnswerRepository

    @Singleton
    @Binds
    fun bindDictionaryRepository(impl: DictionaryRepositoryImpl): DictionaryRepository

    @Singleton
    @Binds
    fun bindAppSettingsRepository(impl: AppSettingsRepositoryImpl): AppSettingsRepository
}