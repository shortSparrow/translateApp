package com.ovolk.dictionary.data.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkerParameters
import com.ovolk.dictionary.domain.use_case.exam.GetWordsForDelayedUpdatePriorityUseCase
import com.ovolk.dictionary.domain.use_case.exam.UpdateWordPriorityUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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

        fun getWorker(): OneTimeWorkRequest {
            return OneTimeWorkRequestBuilder<UpdateWordsPriorityWorker>()
                .build()
        }
    }

}
