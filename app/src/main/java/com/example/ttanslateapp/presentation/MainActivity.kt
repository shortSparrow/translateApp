package com.example.ttanslateapp.presentation

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.ttanslateapp.R
import com.example.ttanslateapp.presentation.exam.ExamReminder
import com.example.ttanslateapp.presentation.modify_word.ModifyWordModes
import com.example.ttanslateapp.presentation.word_list.WordListFragmentDirections
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch
import javax.inject.Inject


class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var examReminder: ExamReminder
    lateinit var listener: NavController.OnDestinationChangedListener
    private lateinit var bottomBar: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        (application as TranslateApp).component.inject(this)
        setupNavigation()

        lifecycleScope.launch {
            examReminder.setInitialReminderIfNeeded()
        }

        // get text from selected items
        val text = intent
            .getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT)
        if (text != null) {
            val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
            val navController = navHostFragment.navController

            navController.navigate(
                WordListFragmentDirections.actionWordListFragmentToModifyWordFragment(
                    mode = ModifyWordModes.MODE_ADD, wordValue = text.toString()
                )
            )
        }
    }

    private fun setupNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        bottomBar = findViewById(R.id.bottom_app_bar)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.wordListFragment -> showBottomNav()
                R.id.examKnowledgeWordsFragment -> showBottomNav()
                R.id.settingsFragment -> showBottomNav()
                else -> hideBottomNav()
            }
        }

        bottomBar.setupWithNavController(navController)
    }

    private fun showBottomNav() {
        bottomBar.visibility = View.VISIBLE
    }

    private fun hideBottomNav() {
        bottomBar.visibility = View.GONE
    }
}
