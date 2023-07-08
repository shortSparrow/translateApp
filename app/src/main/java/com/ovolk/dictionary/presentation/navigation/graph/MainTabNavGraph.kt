package com.ovolk.dictionary.presentation.navigation.graph

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.ovolk.dictionary.presentation.exam.ExamScreen
import com.ovolk.dictionary.presentation.lists.ListsScreen
import com.ovolk.dictionary.presentation.settings.SettingsScreen
import com.ovolk.dictionary.presentation.word_list.HomeScreen
import com.ovolk.dictionary.util.DEEP_LINK_BASE


enum class MainTabRotes { EXAM, HOME, LISTS, SETTINGS }
sealed class MainTabBottomBar(val route: String) {
    object Home : MainTabBottomBar("${MainTabRotes.HOME}?searchedWord={searchedWord}")
    object Exam :
        MainTabBottomBar("${MainTabRotes.EXAM}?listName={listName}&listId={listId}&dictionaryId={dictionaryId}")

    object Lists : MainTabBottomBar("${MainTabRotes.LISTS}")
    object Settings : MainTabBottomBar("${MainTabRotes.SETTINGS}")
}


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
                uriPattern =
                    "${DEEP_LINK_BASE}/${MainTabRotes.EXAM}?listName={listName}&listId={listId}"
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
                },
                navArgument("dictionaryId") {
                    type = NavType.LongType
                    defaultValue = -1L
                },
            )
        ) {
            ExamScreen(navController = navController)
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

        commonNavGraph(navController)
    }
}

