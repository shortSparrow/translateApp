package com.example.ttanslateapp.di

import android.app.Application
import com.example.ttanslateapp.data.database.AppDatabase
import com.example.ttanslateapp.data.database.ListsDao
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
}

