package com.ovolk.dictionary.presentation.navigation.graph

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ovolk.dictionary.presentation.navigation.MainTabNavigatorWrapper


object Graph {
    const val ROOT = "root_graph"
    const val WELCOME_SCREEN = "welcome_screen"
    const val MAIN_TAB_BAR = "main_tab_bar"
    const val COMMON = "common"
}

@Composable
fun RootNavigationGraph(navController: NavHostController, isWelcomeScreenPassed: Boolean) {

    val startDestination = if (isWelcomeScreenPassed) {
        Graph.MAIN_TAB_BAR
    } else {
        Graph.WELCOME_SCREEN
    }

    NavHost(
        navController = navController,
        route = Graph.ROOT,
        startDestination = startDestination
    ) {
        welcomeScreenGraph(navController = navController)
        composable(route = Graph.MAIN_TAB_BAR) {
            MainTabNavigatorWrapper()
        }
    }
}
