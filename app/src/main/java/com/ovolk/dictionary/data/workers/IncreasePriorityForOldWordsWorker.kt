package com.ovolk.dictionary.data.workers

import android.content.Context
import androidx.work.*
import java.util.concurrent.TimeUnit


class IncreasePriorityForOldWordsWorker(
    val context: Context,
    workerParameters: WorkerParameters,
    private val handleOldWordsPriority: HandleOldWordsPriority,
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        return try {
            handleOldWordsPriority.updatePriorityForBunchOldWords()
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }

    companion object {
        const val NAME = "IncreasePriorityForOldWordsWorker"

        fun getWorker(): PeriodicWorkRequest {
            return PeriodicWorkRequestBuilder<IncreasePriorityForOldWordsWorker>(
                3,
                TimeUnit.DAYS
            )
                .build()
        }
    }
}