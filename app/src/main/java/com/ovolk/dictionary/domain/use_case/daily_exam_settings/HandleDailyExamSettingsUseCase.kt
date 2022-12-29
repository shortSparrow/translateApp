package com.ovolk.dictionary.domain.use_case.daily_exam_settings

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import com.ovolk.dictionary.presentation.settings_exam_daily.SettingsExamDailyAction
import com.ovolk.dictionary.util.DAILY_EXAM_SETTINGS
import com.ovolk.dictionary.util.DEFAULT_DAILY_EXAM_WORDS_COUNT
import com.ovolk.dictionary.util.SETTINGS_PREFERENCES
import javax.inject.Inject

data class DailyExamSettings(
    val countOfWords: String
)

class HandleDailyExamSettingsUseCase @Inject constructor(
    private val application: Application
) {
    fun getDailyExamSettings(): DailyExamSettings {
        val countOfWords = application.getSharedPreferences(
            SETTINGS_PREFERENCES,
            AppCompatActivity.MODE_PRIVATE
        )
            .getString(DAILY_EXAM_SETTINGS, DEFAULT_DAILY_EXAM_WORDS_COUNT.toString())
            ?: DEFAULT_DAILY_EXAM_WORDS_COUNT.toString()
        return DailyExamSettings(countOfWords = countOfWords)
    }

    fun saveDailyExamSettings(dailyNumberWords: String) {
        application.getSharedPreferences(
            SETTINGS_PREFERENCES,
            AppCompatActivity.MODE_PRIVATE
        )
            .edit()
            .apply {
                putString(DAILY_EXAM_SETTINGS, dailyNumberWords)
                apply()
            }

    }
}