package com.ovolk.dictionary.data.database.app_settings

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import com.ovolk.dictionary.domain.use_case.exam_remibder.GetTimeReminder
import com.ovolk.dictionary.util.APP_SETTINGS
import com.ovolk.dictionary.util.DAILY_EXAM_SETTINGS
import com.ovolk.dictionary.util.DEFAULT_DAILY_EXAM_WORDS_COUNT
import com.ovolk.dictionary.util.DEFAULT_IS_DOUBLE_LANGUAGE_EXAM_ENABLE
import com.ovolk.dictionary.util.EXAM_REMINDER_FREQUENCY
import com.ovolk.dictionary.util.EXAM_REMINDER_TIME
import com.ovolk.dictionary.util.IS_CHOOSE_LANGUAGE
import com.ovolk.dictionary.util.IS_DOUBLE_LANGUAGE_EXAM_ENABLE
import com.ovolk.dictionary.util.IS_WELCOME_SCREEN_PASSED
import com.ovolk.dictionary.util.PushFrequency
import com.ovolk.dictionary.util.SETTINGS_PREFERENCES
import com.ovolk.dictionary.util.SETTINGS_VERSION
import com.ovolk.dictionary.util.USER_STATE_PREFERENCES
import javax.inject.Inject

class AppSettingsMigration @Inject constructor(val application: Application) {
    private val appSettingsPreferences: SharedPreferences = application.getSharedPreferences(
        APP_SETTINGS,
        AppCompatActivity.MODE_PRIVATE
    )

    private val userStatePreferences: SharedPreferences = application.getSharedPreferences(
        USER_STATE_PREFERENCES,
        AppCompatActivity.MODE_PRIVATE
    )

    private val userSettingsPreferences: SharedPreferences = application.getSharedPreferences(
        SETTINGS_PREFERENCES,
        AppCompatActivity.MODE_PRIVATE
    )

    fun runMigrationIfNeeded() {
        val settingsVersion = appSettingsPreferences.getInt(SETTINGS_VERSION, 0)

        /**
         * runs for every updated  app version from 1.0.5 to 1.0.6 and for first installation
         */
        if (settingsVersion == 0) {
            migrationFrom1To2()
        }
    }

    private fun migrationFrom1To2() {
        val isChosenLanguage = userStatePreferences.getBoolean(IS_CHOOSE_LANGUAGE, false)

        val reminderFrequency =
            userSettingsPreferences.getInt(EXAM_REMINDER_FREQUENCY, PushFrequency.ONCE_AT_DAY)
        val examReminderTime = userSettingsPreferences.getString(
            EXAM_REMINDER_TIME,
            GetTimeReminder.defaultValue
        )

        val isDoubleLanguageExamEnable = userSettingsPreferences.getBoolean(
            IS_DOUBLE_LANGUAGE_EXAM_ENABLE,
            DEFAULT_IS_DOUBLE_LANGUAGE_EXAM_ENABLE
        )

        val countOfWords = userSettingsPreferences.getString(
            DAILY_EXAM_SETTINGS,
            DEFAULT_DAILY_EXAM_WORDS_COUNT.toString()
        ) ?: DEFAULT_DAILY_EXAM_WORDS_COUNT.toString()


        appSettingsPreferences.edit()
            .apply {
                putBoolean(IS_WELCOME_SCREEN_PASSED, isChosenLanguage)
                putInt(EXAM_REMINDER_FREQUENCY, reminderFrequency)
                if (examReminderTime != null) {
                    putString(EXAM_REMINDER_TIME, examReminderTime)
                }
                putBoolean(IS_DOUBLE_LANGUAGE_EXAM_ENABLE, isDoubleLanguageExamEnable)
                putString(DAILY_EXAM_SETTINGS, countOfWords)
                putInt(SETTINGS_VERSION, 1)
                commit()
            }.apply {
                deleteOldPreferences()
            }
    }

    private fun deleteOldPreferences() {
        application.deleteSharedPreferences(SETTINGS_PREFERENCES)
        application.deleteSharedPreferences(USER_STATE_PREFERENCES)
    }

}