package com.ovolk.dictionary.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.ovolk.dictionary.presentation.exam.ExamScreen
import com.ovolk.dictionary.presentation.modify_word.ModifyWordModes
import com.ovolk.dictionary.presentation.modify_word.ModifyWordScreen
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

        homeNavGraph(navController)
    }
}

enum class MainTabRotes { EXAM, HOME }
sealed class MainTabBottomBar(val route: String) {
    object Home : MainTabBottomBar("${MainTabRotes.HOME}?searchedWord={searchedWord}")
    object Exam : MainTabBottomBar("${MainTabRotes.EXAM}?listName={listName}&listId={listId}")
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