package com.ovolk.dictionary.domain.repositories

import com.ovolk.dictionary.data.database.app_settings.AppSettingsRepositoryImpl
import com.ovolk.dictionary.domain.model.app_settings.AppSettings

interface AppSettingsRepository {
    fun getAppSettings(): AppSettings
    fun setAppSettings(): AppSettingsRepositoryImpl.SaveSettings
}