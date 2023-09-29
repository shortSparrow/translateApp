package com.ovolk.dictionary.presentation

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.work.Configuration
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import com.ovolk.dictionary.BuildConfig
import com.ovolk.dictionary.data.database.app_settings.AppSettingsRepositoryImpl
import com.ovolk.dictionary.data.workers.IncreasePriorityForOldWordsWorker
import com.ovolk.dictionary.data.workers.UpdateWordsPriorityWorker
import com.ovolk.dictionary.domain.use_case.exam_remibder.GetTimeReminder
import com.ovolk.dictionary.domain.use_case.localization.SetAppLanguageUseCase
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

    // Must be set for both DictionaryApp and MainActivity
    // DictionaryApp - needed because sometimes we use DictionaryApp.applicationContext().getString(...)
    // MainActivity - needed because often we use stringResource(...)
    override fun attachBaseContext(base: Context) {
        val appLanguageCode = AppSettingsRepositoryImpl(
            base,
            GetTimeReminder()
        ).getAppSettings().appLanguageCode

        super.attachBaseContext(SetAppLanguageUseCase().setLocale(base, appLanguageCode))
    }


    init {
        instance = this
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        setupUpdateDelayedWordsPriorityIfNeeded()
        setupIncreasePriorityForOldWords()
    }

    private fun setupUpdateDelayedWordsPriorityIfNeeded() {
        val workManager = WorkManager.getInstance(this)
        workManager.enqueueUniqueWork(
            UpdateWordsPriorityWorker.DELAY_UPDATE_WORDS_PRIORITY_NAME,
            ExistingWorkPolicy.APPEND_OR_REPLACE,
            UpdateWordsPriorityWorker.getWorker()
        )
    }

    private fun setupIncreasePriorityForOldWords() {
        val workManager = WorkManager.getInstance(this)
        workManager.enqueueUniquePeriodicWork(
            IncreasePriorityForOldWordsWorker.NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            IncreasePriorityForOldWordsWorker.getWorker()
        )
    }


    companion object {
        private var instance: DictionaryApp? = null

        fun applicationContext(): Context {
            return instance!!.applicationContext
        }
    }
}