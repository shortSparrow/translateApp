package com.ovolk.dictionary.presentation.navigation.graph

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.ovolk.dictionary.presentation.exam.ExamScreen
import com.ovolk.dictionary.presentation.list_full.ListFullScreen
import com.ovolk.dictionary.presentation.lists.ListsScreen
import com.ovolk.dictionary.presentation.modify_word.ModifyWordModes
import com.ovolk.dictionary.presentation.modify_word.ModifyWordScreen
import com.ovolk.dictionary.presentation.settings.SettingsScreen
import com.ovolk.dictionary.presentation.settings_languages.SettingsLanguagesScreen
import com.ovolk.dictionary.presentation.settings_languages_to_from.SettingsLanguagesFromScreen
import com.ovolk.dictionary.presentation.settings_languages_to_from.components.SettingsLanguagesToScreen
import com.ovolk.dictionary.presentation.settings_reminder_exam.ExamReminderScreen
import com.ovolk.dictionary.presentation.word_list.HomeScreen
import com.ovolk.dictionary.util.DEEP_LINK_BASE


@Composable
fun MainTabNavGraph(navController: NavHostController, modifier: Modifier) {

    NavHost(
        navController = navController,
        route = Graph.MAIN_TAB_BAR,
        startDestination = MainTabBottomBar.Home.route,
        modifier = modifier
    ) {
        composable(
            MainTabBottomBar.Home.route,
            deepLinks = listOf(navDeepLink {
                uriPattern = "${DEEP_LINK_BASE}/${MainTabRotes.HOME}/searchedWord={searchedWord}"
            }),
            arguments = listOf(navArgument("searchedWord") {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            })
        ) {
            HomeScreen(navController)
        }

        composable(
            route = MainTabBottomBar.Exam.route,
            deepLinks = listOf(navDeepLink {
                uriPattern = "${DEEP_LINK_BASE}/${MainTabRotes.EXAM}?listName={listName}&listId={listId}"
            }),
            arguments = listOf(
                navArgument("listName") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                },
                navArgument("listId") {
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )
        ) { backStackEntry ->
            val listName = backStackEntry.arguments?.getString("listName") ?: ""
            val listId = backStackEntry.arguments?.getLong("listId") ?: -1L
            ExamScreen(navController = navController, listName = listName, listId = listId)
        }

        composable(route = MainTabBottomBar.Lists.route) {
            ListsScreen(navController = navController)
        }


        composable(route = MainTabBottomBar.Lists.route) {
            ListsScreen(navController = navController)
        }
        composable(route = MainTabBottomBar.Settings.route) {
            SettingsScreen(navController = navController)
        }


        homeNavGraph(navController)
        commonNavGraph(navController)
    }
}

enum class MainTabRotes { EXAM, HOME, LISTS, SETTINGS }
sealed class MainTabBottomBar(val route: String) {
    object Home : MainTabBottomBar("${MainTabRotes.HOME}?searchedWord={searchedWord}")
    object Exam : MainTabBottomBar("${MainTabRotes.EXAM}?listName={listName}&listId={listId}")
    object Lists : MainTabBottomBar("${MainTabRotes.LISTS}")
    object Settings : MainTabBottomBar("${MainTabRotes.SETTINGS}")
}


fun NavGraphBuilder.homeNavGraph(navController: NavHostController) {
    navigation(
        route = Graph.HOME_TAB_NAVIGATOR,
        startDestination = HomeScreens.ModifyWord.route
    ) {
        composable(
            route = HomeScreens.ModifyWord.route,
            deepLinks = listOf(navDeepLink {
                uriPattern = "${DEEP_LINK_BASE}/${HomeRotes.MODIFY_WORD}/wordValue={wordValue}"
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
    }
}

enum class HomeRotes { MODIFY_WORD }
sealed class HomeScreens(val route: String) {
    object ModifyWord :
        HomeScreens("${HomeRotes.MODIFY_WORD}/mode={mode}?wordId={wordId}&wordValue={wordValue}&listId={listId}")
}


fun NavGraphBuilder.commonNavGraph(navController: NavHostController) {
    navigation(
        route = Graph.COMMON,
        startDestination = CommonScreen.FullList.route,
    ) {
        composable(
            route = CommonScreen.FullList.route,
            arguments = listOf(
                navArgument("listName") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                },
                navArgument("listId") {
                    type = NavType.LongType
                    defaultValue = -1L
                }
            ),
        ) { backStackEntry ->
            val listName = backStackEntry.arguments?.getString("listName") ?: ""
            val listId = backStackEntry.arguments?.getLong("listId") ?: -1L
            ListFullScreen(navController = navController, listName = listName, listId = listId)
        }

        composable(route = CommonScreen.SettingsLanguages.route) {
            SettingsLanguagesScreen(navController = navController)
        }

        composable(route = CommonScreen.SettingsLanguagesFrom.route) {
            SettingsLanguagesFromScreen(navController=navController)
        }

        composable(route = CommonScreen.SettingsLanguagesTo.route) {
            SettingsLanguagesToScreen(navController=navController)
        }

        composable(route = CommonScreen.ExamReminder.route,) {
            ExamReminderScreen(navController=navController)
        }
    }
}

enum class CommonRotes { FULL_LIST, SETTINGS_LANGUAGES, SETTINGS_LANGUAGES_FROM, SETTINGS_LANGUAGES_TO, EXAM_REMINDER }
sealed class CommonScreen(val route: String) {
    object FullList : CommonScreen("${CommonRotes.FULL_LIST}?listId={listId}&listName={listName}")
    object SettingsLanguages : CommonScreen("${CommonRotes.SETTINGS_LANGUAGES}")
    object SettingsLanguagesFrom : CommonScreen("${CommonRotes.SETTINGS_LANGUAGES_FROM}")
    object SettingsLanguagesTo : CommonScreen("${CommonRotes.SETTINGS_LANGUAGES_TO}")
    object ExamReminder : CommonScreen("${CommonRotes.EXAM_REMINDER}")
}


