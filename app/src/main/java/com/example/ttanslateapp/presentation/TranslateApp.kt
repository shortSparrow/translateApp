package com.example.ttanslateapp.presentation

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.example.ttanslateapp.BuildConfig
import com.example.ttanslateapp.di.DaggerApplicationComponent
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class TranslateApp : Application() {
    val component = DaggerApplicationComponent.factory().create(this)

    init {
        instance = this
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }


    companion object {
        private var instance: TranslateApp? = null

        fun applicationContext(): Context {
            return instance!!.applicationContext
        }
    }
}