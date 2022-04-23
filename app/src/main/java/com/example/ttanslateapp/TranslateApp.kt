package com.example.ttanslateapp

import android.app.Application
import com.example.ttanslateapp.di.DaggerApplicationComponent

class TranslateApp : Application() {
    val component = DaggerApplicationComponent.factory().create(this)
}