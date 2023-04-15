package com.ovolk.dictionary.data.in_memory_storage

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