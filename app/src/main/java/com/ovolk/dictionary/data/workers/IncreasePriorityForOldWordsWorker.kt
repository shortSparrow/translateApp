package com.ovolk.dictionary.data.workers

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.work.*
import com.ovolk.dictionary.domain.use_case.wokers.HandleOldWordsPriority
import com.ovolk.dictionary.util.SETTINGS_PREFERENCES
import java.util.*
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
                15,
                TimeUnit.DAYS
            )
                .build()
        }
    }
}