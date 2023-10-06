package com.ovolk.dictionary.domain.use_case.daily_exam_settings

import com.ovolk.dictionary.domain.repositories.AppSettingsRepository
import javax.inject.Inject

data class DailyExamSettings(
    val countOfWords: String
)

class HandleDailyExamSettingsUseCase @Inject constructor(
    private val appSettingsRepository: AppSettingsRepository
) {

    fun getDailyExamSettings(): DailyExamSettings {
        val countOfWords = appSettingsRepository.getAppSettings().examCountWords
        return DailyExamSettings(countOfWords = countOfWords)
    }

    fun saveDailyExamSettings(
        dailyNumberWords: String,
        isDoubleLanguageExamEnable: Boolean,
        isAutoSuggestEnable: Boolean
    ) {
        appSettingsRepository.setAppSettings().apply {
            dailyExamWordsCount(dailyNumberWords)
            isDoubleLanguageExamEnable(isDoubleLanguageExamEnable)
            isExamAutoSuggestEnable(isAutoSuggestEnable)
            update()
        }
    }
}