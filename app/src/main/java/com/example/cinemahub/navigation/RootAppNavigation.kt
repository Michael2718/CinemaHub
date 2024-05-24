package com.example.cinemahub.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cinemahub.ui.screens.home.HomeScreen

@Composable
fun RootAppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = Routes.Home.route
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination,
        route = null
    ) {
        composable(Routes.Home.route) {
            HomeScreen()
        }
    }
}