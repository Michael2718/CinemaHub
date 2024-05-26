package com.example.cinemahub.navigation

sealed class Routes(val route: String) {
    data object Home : Routes("home")
    data object Search : Routes("search")
    data object Favorite : Routes("favorite")
    data object Profile : Routes("profile")
    data object Movie : Routes("movie")
}
