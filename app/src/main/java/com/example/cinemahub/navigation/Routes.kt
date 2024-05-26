package com.example.cinemahub.navigation

sealed class Routes(val route: String) {
    data object Splash : Routes("splash")
    data object Welcome : Routes("welcome")

    data object SignIn : Routes("sign_in")
    data object SignUp : Routes("sign_up")

    data object Main : Routes("main")

    data object Home : Routes("home")
    data object Search : Routes("search")
    data object Favorites : Routes("favorites")
    data object Profile : Routes("profile")

    data object HomeGraph : Routes("home_graph")
    data object SearchGraph : Routes("search_graph")
    data object FavoritesGraph : Routes("favorites_graph")
    data object ProfileGraph : Routes("profile_graph")
}

