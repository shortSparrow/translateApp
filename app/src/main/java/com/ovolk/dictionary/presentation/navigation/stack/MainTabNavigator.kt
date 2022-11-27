package com.ovolk.dictionary.presentation.navigation.stack

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ovolk.dictionary.presentation.navigation.MainBottomBar
import com.ovolk.dictionary.presentation.navigation.graph.MainTabNavGraph

@Composable
fun MainTabNavigator(navController: NavHostController = rememberNavController()) {
    Scaffold(bottomBar = { MainBottomBar(navController) }) { innerPadding ->
        MainTabNavGraph(navController = navController, modifier = Modifier.padding(innerPadding))
    }
}
