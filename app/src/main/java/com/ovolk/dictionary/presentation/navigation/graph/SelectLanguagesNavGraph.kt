package com.ovolk.dictionary.presentation.navigation.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.ovolk.dictionary.presentation.select_languages.LangFromScreen
import com.ovolk.dictionary.presentation.select_languages.LanguagesToScreen

fun NavGraphBuilder.selectLanguagesGraph(navController: NavHostController) {
    navigation(
        route = Graph.SELECT_LANGUAGES,
        startDestination = SelectLanguagesScreens.LangFrom.route
    ) {
        composable(route = SelectLanguagesScreens.LangFrom.route) {
            LangFromScreen(navController)
        }

        composable(route = SelectLanguagesScreens.LangTo.route) {
            LanguagesToScreen(navController)
        }
    }
}

sealed class SelectLanguagesScreens(val route: String) {
    object LangFrom : SelectLanguagesScreens("LANGUAGES_FROM")
    object LangTo : SelectLanguagesScreens("LANGUAGES_TO")
}