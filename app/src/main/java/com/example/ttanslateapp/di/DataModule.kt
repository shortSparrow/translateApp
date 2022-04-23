package com.example.ttanslateapp.di

import android.app.Application
import android.content.Context
import com.example.ttanslateapp.data.AppDatabase
import com.example.ttanslateapp.data.TranslatedWordDao
import com.example.ttanslateapp.data.TranslatedWordRepositoryImpl
import com.example.ttanslateapp.domain.TranslatedWordRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DataModule {

    @ApplicationScope
    @Binds
    fun bindTranslatedWordRepository(impl: TranslatedWordRepositoryImpl): TranslatedWordRepository

    companion object {
        @Provides
        @ApplicationScope
        fun provideTranslatedWordDao(context: Application): TranslatedWordDao {
            return AppDatabase.getInstance(context).translatedWordDao()
        }
    }


}