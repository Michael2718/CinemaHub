package com.example.cinemahub.navigation

import kotlinx.serialization.Serializable

sealed class Routes(val route: String) {
    data object Splash : Routes("splash")
    data object Welcome : Routes("welcome")
    data object Root : Routes("root")

    data object SignIn : Routes("sign_in")
    data object SignUp : Routes("sign_up")

    data object Main : Routes("main")

    data object HomeGraph : Routes("home_graph")
    data object Home : Routes("home")

    data object SearchGraph : Routes("search_graph")
    data object Search : Routes("search")
    data object SearchFilter : Routes("search_filter")

    data object FavoritesGraph : Routes("favorites_graph")
    data object Favorites : Routes("favorites")

    data object ProfileGraph : Routes("profile_graph")
    data object Profile : Routes("profile")
}

@Serializable
data class MovieDetails(
    val movieId: String
)
