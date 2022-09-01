package com.example.ttanslateapp.di

import com.example.ttanslateapp.data.database.ExamWordAnswerRepositoryImpl
import com.example.ttanslateapp.data.database.ListsRepositoryImpl
import com.example.ttanslateapp.data.database.TranslatedWordRepositoryImpl
import com.example.ttanslateapp.domain.ExamWordAnswerRepository
import com.example.ttanslateapp.domain.TranslatedWordRepository
import com.example.ttanslateapp.domain.use_case.lists.ListsRepository
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