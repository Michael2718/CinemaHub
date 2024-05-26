package com.example.cinemahub.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.cinemahub.ui.screens.favorites.FavoritesScreen
import com.example.cinemahub.ui.screens.home.HomeScreen
import com.example.cinemahub.ui.screens.profile.ProfileScreen
import com.example.cinemahub.ui.screens.search.SearchScreen

@Composable
fun MainAppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String = Routes.HomeGraph.route
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        homeGraph(
            navController = navController
        )
        searchGraph(
            navController = navController
        )
        favoritesGraph(
            navController = navController
        )
        profileGraph(
            navController = navController
        )
    }
}

fun NavGraphBuilder.homeGraph(
    navController: NavHostController
) {
    navigation(startDestination = Routes.Home.route, route = Routes.HomeGraph.route) {
        composable(Routes.Home.route) {
            HomeScreen()
        }
    }
}

fun NavGraphBuilder.searchGraph(
    navController: NavHostController
) {
    navigation(startDestination = Routes.Search.route, route = Routes.SearchGraph.route) {
        composable(Routes.Search.route) {
            SearchScreen()
        }
    }
}

fun NavGraphBuilder.favoritesGraph(
    navController: NavHostController
) {
    navigation(startDestination = Routes.Favorites.route, route = Routes.FavoritesGraph.route) {
        composable(Routes.Favorites.route) {
            FavoritesScreen()
        }
    }
}

fun NavGraphBuilder.profileGraph(
    navController: NavHostController
) {
    navigation(startDestination = Routes.Profile.route, route = Routes.ProfileGraph.route) {
        composable(Routes.Profile.route) {
            ProfileScreen()
        }
    }
}
