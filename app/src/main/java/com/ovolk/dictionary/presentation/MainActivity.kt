package com.ovolk.dictionary.presentation

import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.appcompattheme.AppCompatTheme
import com.ovolk.dictionary.domain.use_case.word_list.GetSearchedWordListUseCase
import com.ovolk.dictionary.domain.ExamReminder
import com.ovolk.dictionary.presentation.navigation.graph.HomeRotes
import com.ovolk.dictionary.presentation.navigation.graph.MainTabRotes
import com.ovolk.dictionary.presentation.navigation.graph.RootNavigationGraph
import com.ovolk.dictionary.util.DEEP_LINK_BASE
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

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        setupNavigation()

        lifecycleScope.launch {
            setShowVariantsExamAvailableLanguagesIdNeeded(application)
            examReminder.setInitialReminderIfNeeded()
        }

        setContent {
            val navController = rememberNavController()
            AppCompatTheme {
                RootNavigationGraph(navController = navController)
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        // when open app from intent and app already active (in background fro example)
        setupInitialDestination(intent)
    }

    private fun setupNavigation() {
        // should invoke only for onCreate, but not for onNewIntent
        if (!getIsChosenLanguage()) {
            // TODO check and maybe add deep link navigation here
//            navController.navigate(WordListFragmentDirections.actionWordListFragmentToLanguagesFromFragment())
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

        // get text from selected items
        val text = intent?.getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT)
        val context = DictionaryApp.applicationContext()

        if (text != null && getIsChosenLanguage()) {
            lifecycleScope.launch {
                val searchedValue =
                    getSearchedWordListUseCase.getExactSearchedWord(text.toString())

                if (searchedValue == null) {
                    val deepLinkIntent = Intent(
                        Intent.ACTION_VIEW,
                        "${DEEP_LINK_BASE}/${HomeRotes.MODIFY_WORD}/wordValue=$text".toUri(),
                        context,
                        MainActivity::class.java
                    )

                    val deepLinkPendingIntent: PendingIntent? =
                        TaskStackBuilder.create(context).run {
                            addNextIntentWithParentStack(deepLinkIntent)
                            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
                        }
//                deepLinkPendingIntent?.send()
                } else {
                    val deepLinkIntent = Intent(
                        Intent.ACTION_VIEW,
                        "${DEEP_LINK_BASE}/${MainTabRotes.HOME}/searchedWord=$text".toUri(),
                        context,
                        MainActivity::class.java
                    )

                    val deepLinkPendingIntent: PendingIntent? =
                        TaskStackBuilder.create(context).run {
                            addNextIntentWithParentStack(deepLinkIntent)
                            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
                        }
//                deepLinkPendingIntent?.send()
                }
            }
        }
    }

}
