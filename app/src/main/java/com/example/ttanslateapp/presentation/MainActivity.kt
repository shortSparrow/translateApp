package com.example.ttanslateapp.presentation

import android.app.AlarmManager
import android.app.PendingIntent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.ttanslateapp.R
import com.example.ttanslateapp.data.workers.AlarmReceiver
import com.example.ttanslateapp.data.workers.DailyWorker
import com.example.ttanslateapp.presentation.word_list.WordListFragment
import com.example.ttanslateapp.util.getExamWorkerDelay
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        // clear work manger by tag
//        WorkManager.getInstance(applicationContext).cancelAllWorkByTag(DailyWorker.TAG)


//        val dailyWorkRequest = OneTimeWorkRequestBuilder<DailyWorker>()
//            .setInitialDelay(getExamWorkerDelay(), TimeUnit.MILLISECONDS)
//            .addTag(DailyWorker.TAG)
//            .build()
//
//        WorkManager.getInstance(applicationContext)
//            .enqueueUniqueWork(
//                DailyWorker.NAME,
//                ExistingWorkPolicy.REPLACE,
//                dailyWorkRequest
//            )

        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MILLISECOND, getExamWorkerDelay().toInt())

        val intent = AlarmReceiver.newIntent(this)

        val pendingIntent = PendingIntent.getBroadcast(this, 100, intent, PendingIntent.FLAG_IMMUTABLE)
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)

    }
}