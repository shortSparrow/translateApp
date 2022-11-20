package com.ovolk.dictionary.presentation.navigation

import androidx.compose.foundation.background
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
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
import com.ovolk.dictionary.R
import com.ovolk.dictionary.presentation.navigation.graph.MainTabBottomBar


data class MainBottomBarItem(
    val text: String,
    val icon: Painter,
    val route: MainTabBottomBar
)

@Composable
fun MainBottomBar(navController: NavHostController) {
    val screens = listOf(
        MainBottomBarItem(
            route = MainTabBottomBar.Home,
            text = stringResource(id = R.string.home),
            icon = painterResource(id = R.drawable.home)
        ),
        MainBottomBarItem(
            route = MainTabBottomBar.Exam,
            text = stringResource(id = R.string.exam),
            icon = painterResource(id = R.drawable.exam)
        ),
        MainBottomBarItem(
            route = MainTabBottomBar.Lists,
            text = stringResource(id = R.string.lists),
            icon = painterResource(id = R.drawable.list)
        ),
        MainBottomBarItem(
            route = MainTabBottomBar.Settings,
            text = stringResource(id = R.string.settings),
            icon = painterResource(id = R.drawable.settings)
        )
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val bottomBarDestination = screens.any { it.route.route == currentDestination?.route }

    if (bottomBarDestination) {
        BottomNavigation {
            screens.forEach { screen ->
                val selected =
                    currentDestination?.hierarchy?.any { it.route == screen.route.route } == true

                BottomNavigationItem(
                    icon = { Icon(screen.icon, contentDescription = null) },
                    label = { Text(screen.text) },
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
                            restoreState = false
                        }
                    },
                    selectedContentColor = colorResource(id = R.color.blue),
                    unselectedContentColor = colorResource(id = R.color.grey),
                    modifier = Modifier.background(Color.White),
                )
            }
        }

    }
}