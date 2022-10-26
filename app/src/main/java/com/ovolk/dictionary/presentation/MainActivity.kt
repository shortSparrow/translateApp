package com.ovolk.dictionary.presentation

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewTreeObserver
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.forEach
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ovolk.dictionary.R
import com.ovolk.dictionary.domain.use_case.word_list.GetSearchedWordListUseCase
import com.ovolk.dictionary.presentation.exam.ExamReminder
import com.ovolk.dictionary.presentation.modify_word.ModifyWordModes
import com.ovolk.dictionary.presentation.word_list.WordListFragmentDirections
import com.ovolk.dictionary.util.IS_CHOOSE_LANGUAGE
import com.ovolk.dictionary.util.USER_STATE_PREFERENCES
import com.ovolk.dictionary.util.helpers.setShowVariantsExamAvailableLanguagesIdNeeded
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var examReminder: ExamReminder

    @Inject
    lateinit var getSearchedWordListUseCase: GetSearchedWordListUseCase

    lateinit var listener: NavController.OnDestinationChangedListener
    private lateinit var bottomBar: BottomNavigationView
    var isAppReady = false

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupNavigation()

        lifecycleScope.launch {
            setShowVariantsExamAvailableLanguagesIdNeeded(application)
            examReminder.setInitialReminderIfNeeded()
        }
        hideSplashScreen()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        // when open app from intent and app already active (in background fro example)
        setupInitialDestination(intent)
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
            "Settings" to listOf(
                "SettingsFragment",
                "SettingsLanguagesFragment",
                "ExamReminderFragment",
                "SettingsLanguagesFromFragment",
                "SettingsLanguagesToFragment"
            ),
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
                R.id.wordListFragment,
                R.id.examKnowledgeWordsFragment,
                R.id.listFragment,
                R.id.settingsFragment -> {
                    true
                }
                else -> false
            }
        }

        // should invoke only for onCreate, but not for onNewIntent
        if (!getIsChosenLanguage()) {
            navController.navigate(WordListFragmentDirections.actionWordListFragmentToLanguagesFromFragment())
        }
        setupInitialDestination(intent)
    }


    private fun getIsChosenLanguage(): Boolean {
        val userStatePreferences: SharedPreferences = application.getSharedPreferences(
            USER_STATE_PREFERENCES,
            MODE_PRIVATE
        )

        return userStatePreferences.getBoolean(IS_CHOOSE_LANGUAGE, false)
    }


    private fun setupInitialDestination(intent: Intent?) {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        // get text from selected items
        val text = intent?.getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT)

        if (text != null && getIsChosenLanguage()) {
            lifecycleScope.launch {
                val searchedValue =
                    getSearchedWordListUseCase.getExactSearchedWord(text.toString())

                if (searchedValue == null) {
                    val bundle = bundleOf(
                        "mode" to ModifyWordModes.MODE_ADD,
                        "wordValue" to text.toString()
                    )
                    navController.navigate(
                        R.id.modifyWordFragment,
                        bundle,
                        NavOptions.Builder().setPopUpTo(R.id.modifyWordFragment, true)
                            .build()
                    )
                } else {
                    val bundle = bundleOf(
                        "searchedWord" to text,
                    )
                    navController.navigate(
                        R.id.wordListFragment,
                        bundle,
                        NavOptions.Builder().setPopUpTo(R.id.wordListFragment, true)
                            .build()
                    )
                }

                // without Handler calls before navigation works
                Handler(Looper.getMainLooper()).post {
                    isAppReady = true
                }
            }
        } else {
            isAppReady = true
        }
    }


    private fun showBottomNav() {
        bottomBar.visibility = View.VISIBLE
    }

    private fun hideBottomNav() {
        bottomBar.visibility = View.GONE
    }

    private fun hideSplashScreen() {
        val content: View = findViewById(android.R.id.content)
        content.viewTreeObserver.addOnPreDrawListener(
            object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    return if (isAppReady) {
                        content.viewTreeObserver.removeOnPreDrawListener(this)
                        true
                    } else {
                        false
                    }
                }
            }
        )
    }
}
