package com.example.ttanslateapp.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.ttanslateapp.R
import com.example.ttanslateapp.data.workers.DailyWorker
import com.example.ttanslateapp.presentation.word_list.WordListFragment
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            launchModifyFragment()
        }


//        // clear work manger by tag
//        WorkManager.getInstance(applicationContext).cancelAllWorkByTag(DailyWorker.TAG)

//        val dailyWorkRequest = OneTimeWorkRequestBuilder<DailyWorker>()
//            .setInitialDelay(0L, TimeUnit.MILLISECONDS)
//            .addTag(DailyWorker.TAG)
//            .build()
//
//        WorkManager.getInstance(applicationContext)
//            .enqueueUniqueWork(
//                DailyWorker.NAME,
//                ExistingWorkPolicy.REPLACE,
//                dailyWorkRequest
//            )


    }


    private fun launchModifyFragment() {
        val fragment = WordListFragment.newInstance()
        supportFragmentManager.popBackStack()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.root_container, fragment)
            .commit()
    }

}