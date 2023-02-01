package com.ovolk.dictionary.di

import androidx.work.*
import com.ovolk.dictionary.data.workers.DictionaryAppDelegateWorkerFactory
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
        dictionaryAppDelegateWorkerFactory: DictionaryAppDelegateWorkerFactory
    ): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(dictionaryAppDelegateWorkerFactory)
            .build()
    }
}