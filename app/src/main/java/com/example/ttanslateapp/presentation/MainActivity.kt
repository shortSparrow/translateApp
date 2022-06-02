package com.example.ttanslateapp.presentation

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.example.ttanslateapp.R
import com.example.ttanslateapp.presentation.exam.ExamReminder
import com.google.android.material.appbar.MaterialToolbar
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var examReminder: ExamReminder

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        (application as TranslateApp).component.inject(this)

        setSupportActionBar(findViewById(R.id.topAppBar))
        topAppBarClickListener()

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

        examReminder.setInitialReminderIfNeeded()
    }

    private fun topAppBarClickListener() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        val topAppBar = findViewById<MaterialToolbar>(R.id.topAppBar)

        topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.settings -> {
                    navController.navigate(R.id.settingsFragment)
                    true
                }
                R.id.exam -> {
                    navController.navigate(R.id.examKnowledgeWordsFragment)
                    true
                }
                else -> false
            }
        }
    }
}