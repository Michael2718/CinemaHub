package com.example.cinemahub.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PlayArrow
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
import com.example.cinemahub.navigation.MainAdminAppNavigation
import com.example.cinemahub.navigation.MainUserAppNavigation
import com.example.cinemahub.navigation.routes.FavoritesGraph
import com.example.cinemahub.navigation.routes.HomeGraph
import com.example.cinemahub.navigation.routes.MoviesGraph
import com.example.cinemahub.navigation.routes.ProfileGraph
import com.example.cinemahub.navigation.routes.SearchGraph
import com.example.cinemahub.navigation.routes.UsersGraph

@Composable
fun MainScreen(
    onLogOut: () -> Unit,
    navGraphs: List<Any>,
    isAdmin: Boolean,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination

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
        if (isAdmin) {
            MainAdminAppNavigation(
                navController = navController,
                onLogOut = onLogOut,
                modifier = modifier
                    .fillMaxSize()
                    .padding(it)
            )
        } else {
            MainUserAppNavigation(
                navController = navController,
                onLogOut = onLogOut,
                modifier = modifier
                    .fillMaxSize()
                    .padding(it)
            )
        }
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
                selected = currentDestination?.hierarchy?.any {
                    it.route?.contains(navItem.toString())
                        ?: false
                } == true,
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
                            MoviesGraph -> Icons.Filled.PlayArrow
                            UsersGraph -> Icons.Filled.AccountCircle
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
