package com.ovolk.dictionary.presentation.navigation.stack

import androidx.navigation.*
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.ovolk.dictionary.presentation.list_full.ListFullScreen
import com.ovolk.dictionary.presentation.modify_dictionary.ModifyDictionaryModes
import com.ovolk.dictionary.presentation.modify_dictionary.ModifyDictionaryScreen
import com.ovolk.dictionary.presentation.modify_word.ModifyWordModes
import com.ovolk.dictionary.presentation.modify_word.ModifyWordScreen
import com.ovolk.dictionary.presentation.navigation.graph.Graph
import com.ovolk.dictionary.presentation.settings_exam_daily.SettingsExamDailyScreen
import com.ovolk.dictionary.presentation.settings_dictionaries.SettingsDictionariesScreen
import com.ovolk.dictionary.presentation.settings_languages_to_from.SettingsLanguagesFromScreen
import com.ovolk.dictionary.presentation.settings_languages_to_from.components.SettingsLanguagesToScreen
import com.ovolk.dictionary.presentation.settings_reminder_exam.ExamReminderScreen
import com.ovolk.dictionary.util.DEEP_LINK_BASE

enum class CommonRotes { MODIFY_WORD, FULL_LIST, SETTINGS_LANGUAGES, SETTINGS_LANGUAGES_FROM, SETTINGS_LANGUAGES_TO, EXAM_REMINDER, EXAM_DAILY, MODIFY_DICTIONARY }
sealed class CommonScreen(val route: String) {
    object ModifyWord :
        CommonScreen("${CommonRotes.MODIFY_WORD}/mode={mode}?wordId={wordId}&wordValue={wordValue}&listId={listId}")

    object FullList : CommonScreen("${CommonRotes.FULL_LIST}?listId={listId}")
    object SettingsLanguages : CommonScreen("${CommonRotes.SETTINGS_LANGUAGES}")
    object SettingsLanguagesFrom : CommonScreen("${CommonRotes.SETTINGS_LANGUAGES_FROM}")
    object SettingsLanguagesTo : CommonScreen("${CommonRotes.SETTINGS_LANGUAGES_TO}")
    object ExamReminder : CommonScreen("${CommonRotes.EXAM_REMINDER}")
    object ExamDaily : CommonScreen("${CommonRotes.EXAM_DAILY}")
    object ModifyDictionary :
        CommonScreen("${CommonRotes.MODIFY_DICTIONARY}/mode={mode}?dictionaryId={dictionaryId}")
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
                }
            ),
        ) { backStackEntry ->
            val listId = backStackEntry.arguments?.getLong("listId") ?: -1L
            ListFullScreen(navController = navController, listId = listId)
        }

        composable(route = CommonScreen.SettingsLanguages.route) {
            SettingsDictionariesScreen(navController = navController)
        }

        composable(route = CommonScreen.SettingsLanguagesFrom.route) {
            SettingsLanguagesFromScreen(navController = navController)
        }

        composable(route = CommonScreen.SettingsLanguagesTo.route) {
            SettingsLanguagesToScreen(navController = navController)
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
    }
}
