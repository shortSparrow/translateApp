package com.ovolk.dictionary.presentation.navigation.stack

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ovolk.dictionary.R
import com.ovolk.dictionary.presentation.navigation.graph.MainTabBottomBar
import com.ovolk.dictionary.presentation.navigation.graph.MainTabNavGraph

@Composable
fun MainTabNavigator(navController: NavHostController = rememberNavController()) {
    Scaffold(
        bottomBar = { BottomBar(navController) }
    ) { innerPadding ->
        MainTabNavGraph(navController = navController, modifier = Modifier.padding(innerPadding))
    }
}

data class BottomBarItem(
    val text: String,
    val icon: Painter,
    val route: MainTabBottomBar
)

@Composable
fun BottomBar(navController: NavHostController) {
    // TODO programmatically hide
    BottomNavigation {
        val screens = listOf(
            BottomBarItem(
                route = MainTabBottomBar.Home,
                text = stringResource(id = R.string.home),
                icon = painterResource(id = R.drawable.home)
            ),
            BottomBarItem(
                route = MainTabBottomBar.Exam,
                text = stringResource(id = R.string.exam),
                icon = painterResource(id = R.drawable.exam)
            ),
            BottomBarItem(
                route = MainTabBottomBar.Lists,
                text = stringResource(id = R.string.lists),
                icon = painterResource(id = R.drawable.list)
            ),
            BottomBarItem(
                route = MainTabBottomBar.Settings,
                text = stringResource(id = R.string.settings),
                icon = painterResource(id = R.drawable.settings)
            )
        )
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        screens.forEach { screen ->
            val selected =
                currentDestination?.hierarchy?.any { it.route == screen.route.route } == true

            val color =
                if (selected) colorResource(id = R.color.blue) else colorResource(id = R.color.grey)

            BottomNavigationItem(
                icon = { Icon(screen.icon, contentDescription = null, tint = color) },
                label = { Text(screen.text, color = color) },
                selected = selected,
                onClick = {
                    navController.navigate(screen.route.route) {
                        // Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations
                        // on the back stack as users select items
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        // Avoid multiple copies of the same destination when
                        // reselecting the same item
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        restoreState = true
                    }
                },
                selectedContentColor = colorResource(id = R.color.blue),
                unselectedContentColor = colorResource(id = R.color.grey),
                modifier = Modifier.background(Color.White),
            )
        }
    }
}