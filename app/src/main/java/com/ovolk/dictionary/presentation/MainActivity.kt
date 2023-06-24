package com.ovolk.dictionary.presentation

import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.appcompattheme.AppCompatTheme
import com.ovolk.dictionary.data.database.app_settings.AppSettingsMigration
import com.ovolk.dictionary.domain.ExamReminder
import com.ovolk.dictionary.domain.repositories.AppSettingsRepository
import com.ovolk.dictionary.domain.use_case.word_list.GetSearchedWordListUseCase
import com.ovolk.dictionary.presentation.core.snackbar.CustomGlobalSnackbar
import com.ovolk.dictionary.presentation.navigation.graph.MainTabRotes
import com.ovolk.dictionary.presentation.navigation.graph.RootNavigationGraph
import com.ovolk.dictionary.presentation.navigation.stack.CommonRotes
import com.ovolk.dictionary.util.DEEP_LINK_BASE
import com.ovolk.dictionary.util.PASSED_SEARCH_WORD
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var examReminder: ExamReminder

    @Inject
    lateinit var getSearchedWordListUseCase: GetSearchedWordListUseCase

    @Inject
    lateinit var appSettingsRepository: AppSettingsRepository

    private val appSettingsMigration = AppSettingsMigration(DictionaryApp.applicationContext())

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setupNavigation()

        appSettingsMigration.runMigrationIfNeeded() // do it not in coroutine, application must wait until migration will be done

        lifecycleScope.launch {
            examReminder.setInitialReminderIfNeeded()
        }


        setContent {
            val navController = rememberNavController()

            AppCompatTheme {
                RootNavigationGraph(
                    navController = navController,
                    getIsWelcomeScreenPassed = ::getIsWelcomeScreenPassed
                )
                CustomGlobalSnackbar()
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
        setupInitialDestination(intent)
    }

    private fun getIsWelcomeScreenPassed(): Boolean {
        return appSettingsRepository.getAppSettings().isWelcomeScreenPassed
    }


    // todo move to usecase
    private fun setupInitialDestination(intent: Intent?) {
        // get text from selected items
        val text = intent?.getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT)
        val context = DictionaryApp.applicationContext()

        if (text != null && getIsWelcomeScreenPassed()) {
            lifecycleScope.launch {
                val searchedValue =
                    getSearchedWordListUseCase.getExactSearchedWord(text.toString())

                if (searchedValue == null) {
                    val deepLinkIntent = Intent(
                        Intent.ACTION_VIEW,
                        "${DEEP_LINK_BASE}/${CommonRotes.MODIFY_WORD}/wordValue=$text".toUri(),
                        context,
                        MainActivity::class.java
                    )
                    val deepLinkPendingIntent: PendingIntent? =
                        TaskStackBuilder.create(context).run {
                            addNextIntentWithParentStack(deepLinkIntent)
                            getPendingIntent(
                                PASSED_SEARCH_WORD,
                                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                            )
                        }
                    deepLinkPendingIntent?.send()
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
                            getPendingIntent(
                                PASSED_SEARCH_WORD,
                                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                            )
                        }

                    deepLinkPendingIntent?.send()
                }
            }
        }
    }
}
