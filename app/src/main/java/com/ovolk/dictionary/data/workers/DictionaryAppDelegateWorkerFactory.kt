package com.ovolk.dictionary.data.workers

import android.content.Context
import android.util.Log
import androidx.work.DelegatingWorkerFactory
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.ovolk.dictionary.domain.use_case.exam.GetWordsForDelayedUpdatePriorityUseCase
import com.ovolk.dictionary.domain.use_case.exam.UpdateWordPriorityUseCase
import com.ovolk.dictionary.domain.use_case.wokers.HandleOldWordsPriority
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class DictionaryAppDelegateWorkerFactory @Inject constructor(
    updateWordPriorityUseCase: UpdateWordPriorityUseCase,
    getWordsForDelayedUpdatePriorityUseCase: GetWordsForDelayedUpdatePriorityUseCase,
    handleOldWordsPriority: HandleOldWordsPriority,
) : DelegatingWorkerFactory() {
    init {
        addFactory(
            DictionaryAppWorkerFactory(
                updateWordPriorityUseCase = updateWordPriorityUseCase,
                getWordsForDelayedUpdatePriorityUseCase = getWordsForDelayedUpdatePriorityUseCase,
                handleOldWordsPriority = handleOldWordsPriority
            )
        )
    }
}

class DictionaryAppWorkerFactory @Inject constructor(
    private val updateWordPriorityUseCase: UpdateWordPriorityUseCase,
    private val getWordsForDelayedUpdatePriorityUseCase: GetWordsForDelayedUpdatePriorityUseCase,
    private val handleOldWordsPriority: HandleOldWordsPriority,
) : WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        return when (workerClassName) {
            UpdateWordsPriorityWorker::class.java.name -> {
                UpdateWordsPriorityWorker(
                    context = appContext,
                    workerParameters = workerParameters,
                    getWordsForDelayedUpdatePriorityUseCase = getWordsForDelayedUpdatePriorityUseCase,
                    updateWordPriorityUseCase = updateWordPriorityUseCase
                )
            }
            IncreasePriorityForOldWordsWorker::class.java.name -> {
                IncreasePriorityForOldWordsWorker(
                    context = appContext,
                    workerParameters = workerParameters,
                    handleOldWordsPriority = handleOldWordsPriority,
                )
            }
            else -> null
        }
    }
}
