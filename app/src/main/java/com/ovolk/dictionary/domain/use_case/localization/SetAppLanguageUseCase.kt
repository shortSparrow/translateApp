package com.ovolk.dictionary.domain.use_case.localization

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.ovolk.dictionary.domain.repositories.AppSettingsRepository
import com.ovolk.dictionary.presentation.DictionaryApp
import com.ovolk.dictionary.util.DEFAULT_APP_LANGUAGE
import com.ovolk.dictionary.util.supportedAppLanguages
import java.util.Locale
import javax.inject.Inject


class SetAppLanguageUseCase @Inject constructor(
    private val appSettingsRepository: AppSettingsRepository
) {

    fun initializeAppLanguage() {
        val selectedLocale = AppCompatDelegate.getApplicationLocales()[0]

        // user select system language in app settings or this is first installation or API < 33
        if (selectedLocale?.language == null) {
            val systemLocaleLang = Locale.getDefault().language
            val lang =
                if (supportedAppLanguages.any { it.lowercase() == systemLocaleLang.lowercase() }) {
                    systemLocaleLang
                } else {
                    DEFAULT_APP_LANGUAGE
                }

            if (appSettingsRepository.getAppSettings().appLanguageCode != lang) {
                appSettingsRepository.setAppSettings().apply {
                    appLanguage(lang)
                    update()
                }
            }

            return
        }

        if (selectedLocale.language != appSettingsRepository.getAppSettings().appLanguageCode) {
            appSettingsRepository.setAppSettings().apply {
                appLanguage(selectedLocale.language ?: DEFAULT_APP_LANGUAGE)
                update()
            }
        }
    }


    fun setAppLanguage(context: Context, languageCode: String) {
        setLocale(context, languageCode)

        appSettingsRepository.setAppSettings().apply {
            appLanguage(languageCode)
            updateSynchronously()

            val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(languageCode)
            AppCompatDelegate.setApplicationLocales(appLocale)
            relaunchApp(DictionaryApp.applicationContext())
        }
    }


    private fun setLocale(context: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)

        val configuration = context.resources.configuration
        configuration.setLocale(locale)
        configuration.setLayoutDirection(locale)

        return context.createConfigurationContext(configuration)
    }


    private fun relaunchApp(context: Context) {
        val packageManager = context.packageManager
        val intent = packageManager.getLaunchIntentForPackage(context.packageName)
        val componentName = intent!!.component
        val mainIntent = Intent.makeRestartActivityTask(componentName)
        context.startActivity(mainIntent)
        Runtime.getRuntime().exit(0)
    }


}