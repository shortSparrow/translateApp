package com.example.ttanslateapp.presentation

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.forEach
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.ttanslateapp.R
import com.example.ttanslateapp.presentation.exam.ExamKnowledgeWordsFragment
import com.example.ttanslateapp.presentation.exam.ExamReminder
import com.example.ttanslateapp.presentation.modify_word.ModifyWordModes
import com.example.ttanslateapp.presentation.word_list.WordListFragmentDirections
import com.google.android.material.bottomnavigation.BottomNavigationView
import timber.log.Timber
import java.lang.ref.WeakReference
import javax.inject.Inject


class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var examReminder: ExamReminder
    lateinit var listener: NavController.OnDestinationChangedListener
    private lateinit var bottomBar: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        (application as TranslateApp).component.inject(this)
        setupNavigation()
        examReminder.setInitialReminderIfNeeded()


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
