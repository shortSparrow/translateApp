package com.ovolk.dictionary.presentation

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.forEach
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.ovolk.dictionary.R
import com.ovolk.dictionary.presentation.exam.ExamReminder
import com.ovolk.dictionary.presentation.modify_word.ModifyWordModes
import com.ovolk.dictionary.presentation.word_list.WordListFragmentDirections
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var examReminder: ExamReminder
    lateinit var listener: NavController.OnDestinationChangedListener
    private lateinit var bottomBar: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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

        val navigationList = mutableMapOf(
            "Home" to listOf("WordListFragment", "ModifyWordFragment"),
            "Exam" to listOf("ExamKnowledgeWordsFragment"),
            "Lists" to listOf("ListFragment", "ListFullFragment"),
            "Settings" to listOf("SettingsFragment"),
        )

        bottomBar.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.wordListFragment -> {
                    navController.navigate(R.id.wordListFragment)
                    true
                }
                R.id.examKnowledgeWordsFragment -> {
                    navController.navigate(R.id.examKnowledgeWordsFragment)
                    true
                }
                R.id.listFragment -> {
                    navController.navigate(R.id.listFragment)
                    true
                }
                R.id.settingsFragment -> {
                    navController.navigate(R.id.settingsFragment)
                    true
                }
                else -> false
            }
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
//            Timber.d("destination: ${destination.id} ${destination.label}")

            when (destination.id) {
                R.id.wordListFragment -> showBottomNav()
                R.id.examKnowledgeWordsFragment -> showBottomNav()
                R.id.settingsFragment -> showBottomNav()
                R.id.listFragment -> showBottomNav()
                else -> hideBottomNav()
            }

            bottomBar.menu.forEach { item ->
//                Timber.d("TITLE: ${item.title} ${navigationList[item.title]} ${destination.label}")
                if (navigationList[item.title]?.any { it == destination.label } == true) {
                    item.isChecked = true
                }
            }
        }

        bottomBar.setOnItemReselectedListener { item ->
            when (item.itemId) {
                R.id.wordListFragment -> {
                    true
                }
                R.id.examKnowledgeWordsFragment -> {
                    true
                }
                R.id.listFragment -> {
                    true
                }
                R.id.settingsFragment -> {
                    true
                }
                else -> false
            }
        }

    }

    private fun showBottomNav() {
        bottomBar.visibility = View.VISIBLE
    }

    private fun hideBottomNav() {
        bottomBar.visibility = View.GONE
    }
}
