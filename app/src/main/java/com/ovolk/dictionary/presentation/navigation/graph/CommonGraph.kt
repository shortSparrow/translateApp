package com.ovolk.dictionary.presentation.navigation.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.ovolk.dictionary.presentation.dictionary_words.DictionaryWordsScreen
import com.ovolk.dictionary.presentation.list_full.ListFullScreen
import com.ovolk.dictionary.presentation.localization.LocalizationScreen
import com.ovolk.dictionary.presentation.modify_dictionary.ModifyDictionaryModes
import com.ovolk.dictionary.presentation.modify_dictionary.ModifyDictionaryScreen
import com.ovolk.dictionary.presentation.modify_word.ModifyWordModes
import com.ovolk.dictionary.presentation.modify_word.ModifyWordScreen
import com.ovolk.dictionary.presentation.settings_dictionaries.DictionaryListScreen
import com.ovolk.dictionary.presentation.settings_exam_daily.SettingsExamDailyScreen
import com.ovolk.dictionary.presentation.settings_reminder_exam.ExamReminderScreen
import com.ovolk.dictionary.util.DEEP_LINK_BASE

enum class CommonRotes { MODIFY_WORD, FULL_LIST, DICTIONARY_LIST, EXAM_REMINDER, EXAM_DAILY, MODIFY_DICTIONARY, DICTIONARY_WORDS, LOCALIZATION }
sealed class CommonScreen(val route: String) {
    object ModifyWord :
        CommonScreen("${CommonRotes.MODIFY_WORD}/mode={mode}?wordId={wordId}&wordValue={wordValue}&listId={listId}&dictionaryId={dictionaryId}")

    object FullList :
        CommonScreen("${CommonRotes.FULL_LIST}?listId={listId}&dictionaryId={dictionaryId}")

    object DictionaryList : CommonScreen("${CommonRotes.DICTIONARY_LIST}")
    object ExamReminder : CommonScreen("${CommonRotes.EXAM_REMINDER}")
    object ExamDaily : CommonScreen("${CommonRotes.EXAM_DAILY}")
    object ModifyDictionary :
        CommonScreen("${CommonRotes.MODIFY_DICTIONARY}/mode={mode}?dictionaryId={dictionaryId}")

    object DictionaryWords :
        CommonScreen("${CommonRotes.DICTIONARY_WORDS}?dictionaryId={dictionaryId}")

    object Localization :
        CommonScreen("${CommonRotes.LOCALIZATION}")
}


fun NavGraphBuilder.commonNavGraph(navController: NavHostController) {
    navigation(
        route = Graph.COMMON,
        startDestination = CommonScreen.ModifyWord.route,
    ) {
        composable(
            route = CommonScreen.ModifyWord.route,
            deepLinks = listOf(navDeepLink {
                uriPattern = "$DEEP_LINK_BASE/${CommonRotes.MODIFY_WORD}/wordValue={wordValue}"
            }),
            arguments = listOf(
                navArgument("mode") {
                    type = NavType.StringType
                    defaultValue = ModifyWordModes.MODE_ADD.toString()
                },
                navArgument("wordId") {
                    type = NavType.LongType
                    defaultValue = -1L
                },
                navArgument("wordValue") {
                    type = NavType.StringType
                    defaultValue = ""
                },
                navArgument("listId") {
                    type = NavType.LongType
                    defaultValue = -1L
                },
                navArgument("dictionaryId") {
                    type = NavType.LongType
                    defaultValue = -1L
                },
            )
        ) {
            ModifyWordScreen(navController)
        }

        composable(
            route = CommonScreen.FullList.route,
            arguments = listOf(
                navArgument("listId") {
                    type = NavType.LongType
                    defaultValue = -1L
                },
                navArgument("dictionaryId") {
                    type = NavType.LongType
                    defaultValue = -1L
                },
            ),
        ) { backStackEntry ->
            val listId = backStackEntry.arguments?.getLong("listId") ?: -1L
            val dictionaryId = backStackEntry.arguments?.getLong("dictionaryId") ?: -1L
            ListFullScreen(
                navController = navController,
                listId = listId,
                dictionaryId = dictionaryId
            )
        }

        composable(route = CommonScreen.DictionaryList.route) {
            DictionaryListScreen(navController = navController)
        }


        composable(route = CommonScreen.ExamReminder.route) {
            ExamReminderScreen(navController = navController)
        }

        composable(route = CommonScreen.ExamDaily.route) {
            SettingsExamDailyScreen(navController = navController)
        }
        composable(
            route = CommonScreen.ModifyDictionary.route,
            arguments = listOf(
                navArgument("mode") {
                    type = NavType.StringType
                    defaultValue = ModifyDictionaryModes.MODE_ADD.toString()
                },
                navArgument("dictionaryId") {
                    type = NavType.LongType
                    defaultValue = -1L
                },
            )
        ) {
            ModifyDictionaryScreen(navController = navController)
        }

        composable(
            route = CommonScreen.DictionaryWords.route, arguments = listOf(
                navArgument("dictionaryId") {
                    type = NavType.LongType
                    defaultValue = -1L
                },
            )
        ) {
            DictionaryWordsScreen(navController = navController)
        }

        composable(
            route = CommonScreen.Localization.route
        ) {
            LocalizationScreen(navController = navController)
        }
    }
}
