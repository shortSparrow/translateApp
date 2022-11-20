package com.ovolk.dictionary.presentation.navigation.graph

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ovolk.dictionary.presentation.DictionaryApp
import com.ovolk.dictionary.presentation.navigation.stack.MainTabNavigator
import com.ovolk.dictionary.util.IS_CHOOSE_LANGUAGE
import com.ovolk.dictionary.util.USER_STATE_PREFERENCES


object Graph {
    const val ROOT = "root_graph"
    const val SELECT_LANGUAGES = "select_languages"
    const val MAIN_TAB_BAR = "main_tab_bar"
    const val HOME_TAB_NAVIGATOR = "home_tab_navigator"
    const val COMMON = "common"
}

@Composable
fun RootNavigationGraph(navController: NavHostController) {

    fun getIsChosenLanguage(): Boolean {
        val userStatePreferences: SharedPreferences =
            DictionaryApp.applicationContext().getSharedPreferences(
                USER_STATE_PREFERENCES,
                AppCompatActivity.MODE_PRIVATE
            )

        return userStatePreferences.getBoolean(IS_CHOOSE_LANGUAGE, false)
    }


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
