package com.ovolk.dictionary.domain.use_case.word_list

import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Intent
import androidx.core.net.toUri
import androidx.lifecycle.LifecycleCoroutineScope
import com.ovolk.dictionary.domain.repositories.AppSettingsRepository
import com.ovolk.dictionary.presentation.DictionaryApp
import com.ovolk.dictionary.presentation.MainActivity
import com.ovolk.dictionary.presentation.navigation.graph.MainTabRotes
import com.ovolk.dictionary.presentation.navigation.stack.CommonRotes
import com.ovolk.dictionary.util.DEEP_LINK_BASE
import com.ovolk.dictionary.util.PASSED_SEARCH_WORD
import kotlinx.coroutines.launch
import javax.inject.Inject

class SetupInitialDestinationUseCase @Inject constructor(
    private val getSearchedWordListUseCase: GetSearchedWordListUseCase,
    appSettingsRepository: AppSettingsRepository
) {
    private val isWelcomeScreenPassed = appSettingsRepository.getAppSettings().isWelcomeScreenPassed

    fun setup(intent: Intent?, lifecycleScope: LifecycleCoroutineScope) {
        // get text from selected items
        val text = intent?.getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT)
        val context = DictionaryApp.applicationContext()

        if (text != null && isWelcomeScreenPassed) {
            lifecycleScope.launch {
                val searchedValue =
                    getSearchedWordListUseCase.getExactSearchedWord(text.toString())

                if (searchedValue == null) {
                    val deepLinkIntent = Intent(
                        Intent.ACTION_VIEW,
                        "$DEEP_LINK_BASE/${CommonRotes.MODIFY_WORD}/wordValue=$text".toUri(),
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
                        "$DEEP_LINK_BASE/${MainTabRotes.HOME}/searchedWord=$text".toUri(),
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