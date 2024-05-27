package com.example.cinemahub.model.api.movie

import kotlinx.serialization.Serializable

@Serializable
data class MoviesResponse(
    val items: List<Movie>
)
