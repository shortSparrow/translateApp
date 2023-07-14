package com.ovolk.dictionary.presentation.navigation.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.ovolk.dictionary.presentation.create_first_dictionary.CreateFirstDictionary

fun NavGraphBuilder.welcomeScreenGraph(navController: NavHostController) {
    navigation(
        route = Graph.WELCOME_SCREEN,
        startDestination = WelcomeScreens.CreateFirstDictionary.route
    ) {
        composable(route = WelcomeScreens.CreateFirstDictionary.route) {
            CreateFirstDictionary(navController)
        }
    }
}

sealed class WelcomeScreens(val route: String) {
    object CreateFirstDictionary : WelcomeScreens("create_first_dictionary")
}