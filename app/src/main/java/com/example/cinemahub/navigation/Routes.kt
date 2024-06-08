package com.example.cinemahub.navigation

import kotlinx.serialization.Serializable

@Serializable
data object SignIn

@Serializable
data object SignUp

@Serializable
data object MainUser

@Serializable
data object MainAdmin

@Serializable
data object HomeGraph

@Serializable
data object Home

@Serializable
data object SearchGraph

@Serializable
data object Search

@Serializable
data object SearchFilter

@Serializable
data object FavoritesGraph

@Serializable
data object Favorites

@Serializable
data object ProfileGraph

@Serializable
data object Profile

@Serializable
data class MovieDetails(
    val movieId: String
)

@Serializable
data class History(
    val userId: Int
)
