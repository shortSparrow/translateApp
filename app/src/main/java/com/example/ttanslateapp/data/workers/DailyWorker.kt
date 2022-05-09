package com.example.ttanslateapp.data.workers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.work.*
import com.example.ttanslateapp.R
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit

class DailyWorker(val context: Context, params: WorkerParameters) : Worker(context, params) {
    override fun doWork(): Result {
        Timber.d("OK")
        showNotification()

        val dailyWorkRequest = OneTimeWorkRequestBuilder<DailyWorker>()
            .setInitialDelay(15_000L, TimeUnit.MILLISECONDS)
            .addTag(TAG)
            .build()

//        WorkManager.getInstance(applicationContext)
//            .enqueue(dailyWorkRequest)
        WorkManager.getInstance(applicationContext)
            .enqueueUniqueWork(
                NAME,
                ExistingWorkPolicy.REPLACE,
                dailyWorkRequest
            )


        return Result.success()
    }

    private fun showNotification() {
        val notificationManager = context.getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChanel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )

            notificationManager.createNotificationChannel(notificationChanel)
        }

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("Test title2")
            .setContentText("Lol kek2")
            .setSmallIcon(R.drawable.mic_successful)
            .build()


        notificationManager.notify(1, notification)
    }

    companion object {
        const val NAME = "name"
        const val CHANNEL_ID = "test_channel_id"
        private const val CHANNEL_NAME = "channel_name"

        const val TAG = "DailyWorker"
    }
}