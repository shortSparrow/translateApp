package com.ovolk.dictionary.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ovolk.dictionary.presentation.navigation.graph.MainTabNavGraph
import com.ovolk.dictionary.presentation.runtime_permissions.MainRuntimePermission

@Composable
fun MainTabNavigatorWrapper(navController: NavHostController = rememberNavController()) {
    Scaffold(bottomBar = { MainBottomBar(navController) }) { innerPadding ->
        MainTabNavGraph(navController = navController, modifier = Modifier.padding(innerPadding))
        MainRuntimePermission()
    }
}
