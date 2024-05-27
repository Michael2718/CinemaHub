package com.example.cinemahub.ui.screens.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.cinemahub.R
import com.example.cinemahub.navigation.MainAppNavigation
import com.example.cinemahub.navigation.Routes

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = hiltViewModel(),
    navController: NavHostController = rememberNavController()
) {
    val navGraphs: List<Routes> = listOf(
        Routes.HomeGraph,
        Routes.SearchGraph,
        Routes.FavoritesGraph,
        Routes.ProfileGraph,
    )

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination

    Scaffold(
//        topBar = {
//            HomeScreenTopBar()
//        },
        bottomBar = {
            BottomNavigationBar(
                currentDestination = currentDestination,
                onClick = { route ->
                    navController.navigate(route) {
                        val r = route
                        println(r)
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
            modifier = modifier
                .fillMaxSize()
                .padding(it)
        )
    }
}

@Composable
private fun BottomNavigationBar(
    currentDestination: NavDestination?,
    onClick: ((String) -> Unit),
    navGraphs: List<Routes>,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier
    ) {
        for (navItem in navGraphs) {
            NavigationBarItem(
                selected = currentDestination?.hierarchy?.any { it.route == navItem.route } == true,
                onClick = { onClick(navItem.route) },
                icon = {
                    Icon(
                        imageVector = when (navItem) {
                            Routes.HomeGraph -> Icons.Filled.Home
                            Routes.SearchGraph -> Icons.Filled.Search
                            Routes.FavoritesGraph -> Icons.Filled.Favorite
                            Routes.ProfileGraph -> Icons.Filled.AccountCircle
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
    MainScreen(
        navController = rememberNavController()
    )
}
