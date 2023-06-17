package com.ovolk.dictionary.data.database.app_settings

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import com.ovolk.dictionary.domain.model.app_settings.AppSettings
import com.ovolk.dictionary.domain.model.app_settings.ReminderSettings
import com.ovolk.dictionary.domain.repositories.AppSettingsRepository
import com.ovolk.dictionary.domain.use_case.exam_remibder.GetTimeReminder
import com.ovolk.dictionary.util.DAILY_EXAM_SETTINGS
import com.ovolk.dictionary.util.DEFAULT_DAILY_EXAM_WORDS_COUNT
import com.ovolk.dictionary.util.DEFAULT_IS_DOUBLE_LANGUAGE_EXAM_ENABLE
import com.ovolk.dictionary.util.EXAM_REMINDER_FREQUENCY
import com.ovolk.dictionary.util.EXAM_REMINDER_TIME
import com.ovolk.dictionary.util.IS_CHOOSE_LANGUAGE
import com.ovolk.dictionary.util.IS_DOUBLE_LANGUAGE_EXAM_ENABLE
import com.ovolk.dictionary.util.PushFrequency
import com.ovolk.dictionary.util.SETTINGS_PREFERENCES
import com.ovolk.dictionary.util.USER_STATE_PREFERENCES
import com.ovolk.dictionary.util.showVariantsAvailableLanguages
import javax.inject.Inject


class AppSettingsRepositoryImpl @Inject constructor(
    application: Application,
    private val getTimeReminder: GetTimeReminder
) : AppSettingsRepository {
    private val userStatePreferences: SharedPreferences = application.getSharedPreferences(
        USER_STATE_PREFERENCES,
        AppCompatActivity.MODE_PRIVATE
    )

    private val userSettingsPreferences: SharedPreferences = application.getSharedPreferences(
        SETTINGS_PREFERENCES,
        AppCompatActivity.MODE_PRIVATE
    )

    override fun getAppSettings(): AppSettings {
        // TODO rename and make migration
        val isChosenLanguage = userStatePreferences.getBoolean(IS_CHOOSE_LANGUAGE, false)

        val reminderFrequency =
            userSettingsPreferences.getInt(EXAM_REMINDER_FREQUENCY, PushFrequency.ONCE_AT_DAY)
        val examReminderTime = userSettingsPreferences.getString(
            EXAM_REMINDER_TIME,
            GetTimeReminder.defaultValue
        )
            .toString().run {
                getTimeReminder(this)
            }


        val isDoubleLanguageExamEnable = userSettingsPreferences.getBoolean(
            IS_DOUBLE_LANGUAGE_EXAM_ENABLE,
            DEFAULT_IS_DOUBLE_LANGUAGE_EXAM_ENABLE
        )

        val countOfWords = userSettingsPreferences.getString(
            DAILY_EXAM_SETTINGS,
            DEFAULT_DAILY_EXAM_WORDS_COUNT.toString()
        ) ?: DEFAULT_DAILY_EXAM_WORDS_COUNT.toString()


        return AppSettings(
            isWelcomeScreenPassed = isChosenLanguage,
            showVariantsExamAvailableLanguages = showVariantsAvailableLanguages,
            isDoubleLanguageExamEnable = isDoubleLanguageExamEnable,
            examCountWords = countOfWords,
            reminder = ReminderSettings(
                examReminderFrequency = reminderFrequency,
                examReminderTime = examReminderTime
            )
        )
    }

    override fun setAppSettings(): SaveSettings {
        return this.SaveSettings()
    }


    inner class SaveSettings {
        private val userSettingsUpdatedValue = mutableMapOf<String, Any>()
        private val userStateUpdatedValue =
            mutableMapOf<String, Any>() // TODO migrate to userSettingsUpdatedValue

        fun dailyExamWordsCount(value: String) = apply {
            userSettingsUpdatedValue[DAILY_EXAM_SETTINGS] = value
        }

        fun isDoubleLanguageExamEnable(value: Boolean) = apply {
            userSettingsUpdatedValue[IS_DOUBLE_LANGUAGE_EXAM_ENABLE] = value
        }

        fun examReminderFrequency(value: Int) = apply {
            userSettingsUpdatedValue[EXAM_REMINDER_FREQUENCY] = value
        }

        fun examReminderTime(timeGson: String) = apply {
            userSettingsUpdatedValue[EXAM_REMINDER_TIME] = timeGson
        }

        // TODO call this
        fun isChosenLanguage(value: Boolean) = apply {
            userStateUpdatedValue[IS_CHOOSE_LANGUAGE] = value
        }


        fun update() {
            userStatePreferences.edit().apply {
                userStateUpdatedValue.entries.forEach { it ->
                    if (it.value is String) {
                        putString(it.key, (it.value as String))
                    }

                    if (it.value is Int) {
                        putInt(it.key, (it.value as Int))
                    }

                    if (it.value is Long) {
                        putLong(it.key, (it.value as Long))
                    }
                    if (it.value is Float) {
                        putFloat(it.key, (it.value as Float))
                    }

                    if (it.value is Boolean) {
                        putBoolean(it.key, (it.value as Boolean))
                    }
                }
                apply()
            }

            userSettingsPreferences.edit().apply {
                userSettingsUpdatedValue.entries.forEach { it ->
                    if (it.value is String) {
                        putString(it.key, (it.value as String))
                    }

                    if (it.value is Int) {
                        putInt(it.key, (it.value as Int))
                    }

                    if (it.value is Long) {
                        putLong(it.key, (it.value as Long))
                    }
                    if (it.value is Float) {
                        putFloat(it.key, (it.value as Float))
                    }

                    if (it.value is Boolean) {
                        putBoolean(it.key, (it.value as Boolean))
                    }
                }
                apply()
            }

        }
    }


}