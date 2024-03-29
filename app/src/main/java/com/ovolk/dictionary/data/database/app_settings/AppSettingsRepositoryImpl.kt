package com.ovolk.dictionary.data.database.app_settings

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import com.ovolk.dictionary.domain.model.app_settings.AppSettings
import com.ovolk.dictionary.domain.model.app_settings.ReminderSettings
import com.ovolk.dictionary.domain.repositories.AppSettingsRepository
import com.ovolk.dictionary.domain.use_case.exam_remibder.GetTimeReminder
import com.ovolk.dictionary.util.APP_LANGUAGE_CODE
import com.ovolk.dictionary.util.APP_SETTINGS
import com.ovolk.dictionary.util.DAILY_EXAM_SETTINGS
import com.ovolk.dictionary.util.DEFAULT_APP_LANGUAGE
import com.ovolk.dictionary.util.DEFAULT_DAILY_EXAM_WORDS_COUNT
import com.ovolk.dictionary.util.DEFAULT_IS_DOUBLE_LANGUAGE_EXAM_ENABLE
import com.ovolk.dictionary.util.EXAM_REMINDER_FREQUENCY
import com.ovolk.dictionary.util.EXAM_REMINDER_TIME
import com.ovolk.dictionary.util.IS_DOUBLE_LANGUAGE_EXAM_ENABLE
import com.ovolk.dictionary.util.IS_EXAM_AUTO_SUGGEST_ENABLE
import com.ovolk.dictionary.util.IS_WELCOME_SCREEN_PASSED
import com.ovolk.dictionary.util.PERMISSIONS_WAS_REQUESTED_ONCE
import com.ovolk.dictionary.util.PushFrequency
import com.ovolk.dictionary.util.showVariantsAvailableLanguages
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


class AppSettingsRepositoryImpl @Inject constructor(
    @ApplicationContext private val application: Context,
    private val getTimeReminder: GetTimeReminder,
) : AppSettingsRepository {
    private val appSettingsPreferences: SharedPreferences = application.getSharedPreferences(
        APP_SETTINGS,
        AppCompatActivity.MODE_PRIVATE
    )

    override fun getAppSettings(): AppSettings {
        val isWelcomeScreenPassed =
            appSettingsPreferences.getBoolean(IS_WELCOME_SCREEN_PASSED, false)

        val reminderFrequency =
            appSettingsPreferences.getInt(EXAM_REMINDER_FREQUENCY, PushFrequency.ONCE_AT_DAY)
        val examReminderTime = appSettingsPreferences.getString(
            EXAM_REMINDER_TIME,
            GetTimeReminder.defaultValue
        )
            .toString().run {
                getTimeReminder(this)
            }

        val isDoubleLanguageExamEnable = appSettingsPreferences.getBoolean(
            IS_DOUBLE_LANGUAGE_EXAM_ENABLE,
            DEFAULT_IS_DOUBLE_LANGUAGE_EXAM_ENABLE
        )

        val countOfWords = appSettingsPreferences.getString(
            DAILY_EXAM_SETTINGS,
            DEFAULT_DAILY_EXAM_WORDS_COUNT.toString()
        ) ?: DEFAULT_DAILY_EXAM_WORDS_COUNT.toString()

        val isExamAutoSuggestEnable =
            appSettingsPreferences.getBoolean(IS_EXAM_AUTO_SUGGEST_ENABLE, true)

        val appLanguageCode = appSettingsPreferences.getString(
            APP_LANGUAGE_CODE,
            DEFAULT_APP_LANGUAGE
        ) ?: DEFAULT_APP_LANGUAGE

        val permissionsWasRequestedOnce = appSettingsPreferences.getStringSet(
            PERMISSIONS_WAS_REQUESTED_ONCE,
            emptySet<String>()
        ) ?: emptySet<String>()

        return AppSettings(
            isWelcomeScreenPassed = isWelcomeScreenPassed,
            showVariantsExamAvailableLanguages = showVariantsAvailableLanguages,
            isDoubleLanguageExamEnable = isDoubleLanguageExamEnable,
            examCountWords = countOfWords,
            reminder = ReminderSettings(
                examReminderFrequency = reminderFrequency,
                examReminderTime = examReminderTime
            ),
            isExamAutoSuggestEnable = isExamAutoSuggestEnable,
            appLanguageCode = appLanguageCode,
            permissionsWasRequestedOnce = permissionsWasRequestedOnce
        )
    }

    override fun setAppSettings(): SaveSettings {
        return this.SaveSettings()
    }

    override fun removeField(field: String) {
        appSettingsPreferences.edit().remove(field).apply()
    }

    inner class SaveSettings {
        private val appSettingsUpdatedValue = mutableMapOf<String, Any>()


        fun dailyExamWordsCount(value: String) = apply {
            appSettingsUpdatedValue[DAILY_EXAM_SETTINGS] = value
        }

        fun isDoubleLanguageExamEnable(value: Boolean) = apply {
            appSettingsUpdatedValue[IS_DOUBLE_LANGUAGE_EXAM_ENABLE] = value
        }

        fun examReminderFrequency(value: Int) = apply {
            appSettingsUpdatedValue[EXAM_REMINDER_FREQUENCY] = value
        }

        fun examReminderTime(timeGson: String) = apply {
            appSettingsUpdatedValue[EXAM_REMINDER_TIME] = timeGson
        }

        fun isWelcomeScreenPassed(value: Boolean) = apply {
            appSettingsUpdatedValue[IS_WELCOME_SCREEN_PASSED] = value
        }

        fun isExamAutoSuggestEnable(value: Boolean) = apply {
            appSettingsUpdatedValue[IS_EXAM_AUTO_SUGGEST_ENABLE] = value
        }

        fun appLanguage(value: String) = apply {
            appSettingsUpdatedValue[APP_LANGUAGE_CODE] = value
        }

        fun addPermissionWasRequestedOnce(value: String) = apply {
            val data = getAppSettings().permissionsWasRequestedOnce.plus(value)

            appSettingsUpdatedValue[PERMISSIONS_WAS_REQUESTED_ONCE] = data
        }

        fun update(): SharedPreferences.Editor {
            return appSettingsPreferences.edit().apply {
                appSettingsUpdatedValue.entries.forEach { it ->
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

                    if (it.value is Set<*>) {
                        putStringSet(it.key, (it.value as Set<String>))
                    }
                }
                apply()
            }
        }

        fun updateSynchronously() {
            update().apply {
                commit()
            }
        }
    }

}