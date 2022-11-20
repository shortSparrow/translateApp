package com.ovolk.dictionary.presentation.navigation.graph

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ovolk.dictionary.presentation.navigation.stack.MainTabNavigator


object Graph {
    const val ROOT = "root_graph"
    const val SELECT_LANGUAGES = "select_languages"
    const val MAIN_TAB_BAR = "main_tab_bar"
    const val COMMON = "common"
}

@Composable
fun RootNavigationGraph(navController: NavHostController, getIsChosenLanguage: ()-> Boolean) {

    val startDestination = if (getIsChosenLanguage()) {
        Graph.MAIN_TAB_BAR
    } else {
        Graph.SELECT_LANGUAGES
    }

    NavHost(
        navController = navController,
        route = Graph.ROOT,
        startDestination = startDestination
    ) {
        selectLanguagesGraph(navController = navController)
        composable(route = Graph.MAIN_TAB_BAR) {
            MainTabNavigator()
        }
    }
}
