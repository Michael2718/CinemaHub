package com.example.cinemahub.navigation.routes

import kotlinx.serialization.Serializable

@Serializable
data object MainAdmin

@Serializable
data object MoviesGraph

@Serializable
data object Movies

@Serializable
data object AddMovie

@Serializable
data class UpdateMovie(
    val movieId: String
)

//@Serializable
//data object AdminSearchFilters

//@Serializable
//data object ReviewsGraph
//
//@Serializable
//data object Reviews

@Serializable
data object UsersGraph

@Serializable
data object Users
