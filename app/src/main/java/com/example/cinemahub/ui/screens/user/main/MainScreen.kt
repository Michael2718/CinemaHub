package com.example.cinemahub.ui.screens.user.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.cinemahub.PreferenceManagerSingleton
import com.example.cinemahub.navigation.FavoritesGraph
import com.example.cinemahub.navigation.HomeGraph
import com.example.cinemahub.navigation.MainAppNavigation
import com.example.cinemahub.navigation.ProfileGraph
//import com.example.cinemahub.navigation.Routes
import com.example.cinemahub.navigation.SearchGraph

@Composable
fun MainScreen(
    onLogOut: () -> Unit,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
//    val navGraphs: List<Routes> = listOf(
//        Routes.HomeGraph,
//        Routes.SearchGraph,
//        Routes.FavoritesGraph,
//        Routes.ProfileGraph,
//    )

    val navGraphs = listOf(
        HomeGraph,
        SearchGraph,
        FavoritesGraph,
        ProfileGraph,
    )

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination
//    val userId = PreferenceManagerSingleton.getUserId()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                currentDestination = currentDestination,
                onClick = { route ->
                    navController.navigate(route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                navGraphs = navGraphs,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    ) {
        MainAppNavigation(
            navController = navController,
            onLogOut = onLogOut,
            modifier = modifier
                .fillMaxSize()
                .padding(it)
        )
    }
}

@Composable
private fun BottomNavigationBar(
    currentDestination: NavDestination?,
    onClick: ((Any) -> Unit),
    navGraphs: List<Any>,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier
    ) {
        for (navItem in navGraphs) {
            NavigationBarItem(
                selected = currentDestination?.hierarchy?.any { it == navItem } == true,
                onClick = {
                    onClick(navItem)
                },
                icon = {
                    Icon(
                        imageVector = when (navItem) {
                            HomeGraph -> Icons.Filled.Home
                            SearchGraph -> Icons.Filled.Search
                            FavoritesGraph -> Icons.Filled.Favorite
                            ProfileGraph -> Icons.Filled.AccountCircle
                            else -> Icons.Filled.Warning
                        },
                        contentDescription = null
                    )
                },
                alwaysShowLabel = false
            )
        }
    }
}


@Composable
@Preview
fun MainScreenPreview() {
//    MainScreen(
//        navController = rememberNavController()
//    )
}
