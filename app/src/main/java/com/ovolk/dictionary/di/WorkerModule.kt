package com.ovolk.dictionary.di

import androidx.work.*
import com.ovolk.dictionary.data.workers.DictionaryAppWorkerFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object WorkerModule {
    @Singleton
    @Provides
    fun provideWorkManagerConfiguration(
        dictionaryAppWorkerFactory: DictionaryAppWorkerFactory
    ): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(dictionaryAppWorkerFactory)
            .build()
    }
}