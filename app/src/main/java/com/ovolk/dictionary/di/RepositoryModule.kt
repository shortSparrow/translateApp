package com.ovolk.dictionary.di

import com.ovolk.dictionary.data.database.ExamWordAnswerRepositoryImpl
import com.ovolk.dictionary.data.database.ListsRepositoryImpl
import com.ovolk.dictionary.data.database.TranslatedWordRepositoryImpl
import com.ovolk.dictionary.domain.ExamWordAnswerRepository
import com.ovolk.dictionary.domain.TranslatedWordRepository
import com.ovolk.dictionary.domain.use_case.lists.ListsRepository
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
}