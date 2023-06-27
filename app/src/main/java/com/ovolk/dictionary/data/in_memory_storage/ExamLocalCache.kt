package com.ovolk.dictionary.data.in_memory_storage

import com.ovolk.dictionary.di.MyEntryPoint
import com.ovolk.dictionary.presentation.DictionaryApp
import dagger.hilt.EntryPoints
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

enum class ExamStatus { INACTIVE, IN_PROGRESS }

// analog redux in RN
class ExamLocalCache @Inject constructor() {
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

            // inject dependency
            val localCache =
                EntryPoints.get(DictionaryApp.applicationContext(), MyEntryPoint::class.java)
                    .getExamLocalCache()

            INSTANCE = localCache
            return localCache
        }
    }
}