package com.ovolk.dictionary.data.in_memory_storage

import androidx.appcompat.app.AppCompatActivity
import com.ovolk.dictionary.presentation.DictionaryApp
import com.ovolk.dictionary.util.DEFAULT_IS_DOUBLE_LANGUAGE_EXAM_ENABLE
import com.ovolk.dictionary.util.IS_DOUBLE_LANGUAGE_EXAM_ENABLE
import com.ovolk.dictionary.util.SETTINGS_PREFERENCES
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class ExamStatus { INACTIVE, IN_PROGRESS }

// analog redux in RN
class ExamLocalCache {
    var examStatus: ExamStatus = ExamStatus.INACTIVE
        private set

    var interruptedRoute: String? = null
        private set

    private val _isInterruptExamPopupShown = MutableStateFlow(false)
    val isInterruptExamPopupShown = _isInterruptExamPopupShown.asStateFlow()
    private var _isDoubleLanguageExamEnable: Boolean? = null


    fun getIsDoubleLanguageExamEnable(): Boolean {
        return _isDoubleLanguageExamEnable ?: kotlin.run {
            DictionaryApp.applicationContext().getSharedPreferences(
                SETTINGS_PREFERENCES,
                AppCompatActivity.MODE_PRIVATE
            ).getBoolean(IS_DOUBLE_LANGUAGE_EXAM_ENABLE, DEFAULT_IS_DOUBLE_LANGUAGE_EXAM_ENABLE)
        }
    }

    fun setInterruptedRoute(route: String?) {
        interruptedRoute = route
    }

    fun setExamStatus(value: ExamStatus) {
        examStatus = value
    }

    fun setIsInterruptExamPopupShown(value: Boolean) {
        _isInterruptExamPopupShown.value = value
    }

    fun resetToDefault() {
        examStatus = ExamStatus.INACTIVE
        interruptedRoute = null
        _isInterruptExamPopupShown.value = false
    }

    companion object {
        private var INSTANCE: ExamLocalCache? = null

        fun getInstance(): ExamLocalCache {
            INSTANCE?.let {
                return it
            }

            val localCache = ExamLocalCache()
            INSTANCE = localCache
            return localCache
        }
    }
}