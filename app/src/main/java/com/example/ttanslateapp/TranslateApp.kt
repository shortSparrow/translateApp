package com.example.ttanslateapp

import android.app.Application
import com.example.ttanslateapp.di.DaggerApplicationComponent
import timber.log.Timber

class TranslateApp : Application() {
    val component = DaggerApplicationComponent.factory().create(this)

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}