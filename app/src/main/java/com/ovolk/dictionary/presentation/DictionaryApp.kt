package com.ovolk.dictionary.presentation

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.ktx.BuildConfig
import androidx.work.Configuration
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import com.ovolk.dictionary.data.workers.UpdateWordsPriorityWorker
import com.ovolk.dictionary.data.workers.UpdateWordsPriorityWorker.Companion.DELAY_UPDATE_WORDS_PRIORITY_NAME
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject


@HiltAndroidApp
class DictionaryApp : Application(), Configuration.Provider {

    @Inject
    lateinit var workerConfiguration: Configuration

    // Setup custom configuration for WorkManager with a DelegatingWorkerFactory
    override fun getWorkManagerConfiguration(): Configuration {
        return workerConfiguration
    }

    init {
        instance = this
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    override fun onCreate() {
        super.onCreate()
        updateDelayedWordsPriorityIfNeeded()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

    }

    private fun updateDelayedWordsPriorityIfNeeded() {
        val workManager = WorkManager.getInstance(this)
        workManager.enqueueUniqueWork(
            DELAY_UPDATE_WORDS_PRIORITY_NAME,
            ExistingWorkPolicy.APPEND_OR_REPLACE, // Що робити, якщо worker з таким імям вже існує
            UpdateWordsPriorityWorker.invokeDelayUpdateIfNeeded()
        )
    }


    companion object {
        private var instance: DictionaryApp? = null

        fun applicationContext(): Context {
            return instance!!.applicationContext
        }
    }
}