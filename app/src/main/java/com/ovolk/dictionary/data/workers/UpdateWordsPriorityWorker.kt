package com.ovolk.dictionary.data.workers

import android.content.Context
import androidx.work.*
import com.ovolk.dictionary.domain.use_case.exam.GetWordsForDelayedUpdatePriorityUseCase
import com.ovolk.dictionary.domain.use_case.exam.UpdateWordPriorityUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UpdateWordsPriorityWorkerFactory(
    private val updateWordPriorityUseCase: UpdateWordPriorityUseCase,
    private val getWordsForDelayedUpdatePriorityUseCase: GetWordsForDelayedUpdatePriorityUseCase
) : WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker {
        // This only handles a single Worker, please don’t do this!!
        // See below for a better way using DelegatingWorkerFactory
        return UpdateWordsPriorityWorker(
            context = appContext,
            workerParameters = workerParameters,
            getWordsForDelayedUpdatePriorityUseCase = getWordsForDelayedUpdatePriorityUseCase,
            updateWordPriorityUseCase = updateWordPriorityUseCase
        )
    }
}

class UpdateWordsPriorityWorker(
    context: Context,
    workerParameters: WorkerParameters,
    private val getWordsForDelayedUpdatePriorityUseCase: GetWordsForDelayedUpdatePriorityUseCase,
    private val updateWordPriorityUseCase: UpdateWordPriorityUseCase,
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            try {
                val list = getWordsForDelayedUpdatePriorityUseCase()
                if (list.isNotEmpty()) {
                    updateWordPriorityUseCase.updateList(list)
                }
                Result.success()
            } catch (e: Exception) {
                Result.failure()
            }
        }
    }


    companion object {
        const val DELAY_UPDATE_WORDS_PRIORITY_NAME = "DELAY_UPDATE_WORDS_PRIORITY_NAME"

        fun invokeDelayUpdateIfNeeded(): OneTimeWorkRequest {
            return OneTimeWorkRequestBuilder<UpdateWordsPriorityWorker>()
                .build()
        }
    }

}
