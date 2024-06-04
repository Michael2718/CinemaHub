package com.example.cinemahub.model.api.favorite

import com.example.cinemahub.model.api.movie.Movie
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class Favorite(
    val userId: Int,
    val movieId: String,
//    val addedDate: LocalDate
)

@Serializable
data class FavoriteRequest(
    val userId: Int,
    val movieId: String
)

@Serializable
data class FavoriteResponse(
    val movie: Movie,
    val addedDate: LocalDate
)
