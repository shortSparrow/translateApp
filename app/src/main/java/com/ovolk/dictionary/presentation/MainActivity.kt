package com.ovolk.dictionary.presentation

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.appcompattheme.AppCompatTheme
import com.ovolk.dictionary.data.database.app_settings.AppSettingsMigration
import com.ovolk.dictionary.domain.ExamReminder
import com.ovolk.dictionary.domain.repositories.AppSettingsRepository
import com.ovolk.dictionary.domain.use_case.localization.SetAppLanguageUseCase
import com.ovolk.dictionary.domain.use_case.word_list.GetSearchedWordListUseCase
import com.ovolk.dictionary.domain.use_case.word_list.SetupInitialDestinationUseCase
import com.ovolk.dictionary.presentation.core.snackbar.CustomGlobalSnackbar
import com.ovolk.dictionary.presentation.navigation.graph.RootNavigationGraph
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

    @Inject
    lateinit var setupLanguageUseCase: SetAppLanguageUseCase

    @Inject
    lateinit var setupInitialDestinationUseCase: SetupInitialDestinationUseCase

    private val appSettingsMigration = AppSettingsMigration(DictionaryApp.applicationContext())

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setupLanguageUseCase.initializeAppLanguage()
        instance = this
        setupNavigation()

        appSettingsMigration.runMigrationIfNeeded() // do it not in coroutine, application must wait until migration will be done

        lifecycleScope.launch {
            examReminder.setInitialReminderIfNeeded()
        }

        val isWelcomeScreenPassed = appSettingsRepository.getAppSettings().isWelcomeScreenPassed

        setContent {
            val navController = rememberNavController()

            AppCompatTheme {
                RootNavigationGraph(
                    navController = navController,
                    isWelcomeScreenPassed = isWelcomeScreenPassed
                )
                CustomGlobalSnackbar()
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        // when open app from intent and app already active (in background fro example)
        setupInitialDestinationUseCase.setup(intent = intent, lifecycleScope = lifecycleScope)
    }

    private fun setupNavigation() {
        // should invoke only for onCreate, but not for onNewIntent
        setupInitialDestinationUseCase.setup(intent = intent, lifecycleScope = lifecycleScope)
    }

    companion object {
        private var instance: MainActivity? = null

        fun getMainActivity(): Activity {
            return instance!!
        }
    }
}
