package com.example.ttanslateapp.di

import android.app.Application
import com.example.ttanslateapp.data.database.AppDatabase
import com.example.ttanslateapp.data.database.TranslatedWordDao
import com.example.ttanslateapp.data.database.TranslatedWordRepositoryImpl
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
        fun providesTranslatedWordDao(context: Application): TranslatedWordDao {
            return AppDatabase.getInstance(context).translatedWordDao()
        }
    }
}