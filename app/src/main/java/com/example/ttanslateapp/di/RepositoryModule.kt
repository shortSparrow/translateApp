package com.example.ttanslateapp.di

import com.example.ttanslateapp.data.database.ListsRepositoryImpl
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

}