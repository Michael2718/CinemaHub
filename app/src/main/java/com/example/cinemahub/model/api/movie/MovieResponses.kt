package com.example.cinemahub.model.api.movie

import com.example.cinemahub.types.PGIntervalSerializer
import com.example.cinemahub.types.PGMoneySerializer
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import org.postgresql.util.PGInterval
import org.postgresql.util.PGmoney

@Serializable
data class MoviesResponse(
    val items: List<Movie>
)

@Serializable
data class MovieSearchResponse(
    val movieId: String,
    val title: String,
    val releaseDate: LocalDate,
    @Serializable(with = PGIntervalSerializer::class)
    val duration: PGInterval,
    val voteAverage: Double,
    val voteCount: Int,
    val plot: String,
    val isAdult: Boolean,
    val popularity: Int,
    @Serializable(with = PGMoneySerializer::class)
    val price: PGmoney,
    val primaryImageUrl: String,
    val isFavorite: Boolean
)

@Serializable
data class MovieDetailsResponse(
    val movieId: String,
    val title: String,
    val releaseDate: LocalDate,
    @Serializable(with = PGIntervalSerializer::class)
    val duration: PGInterval,
    val voteAverage: Double,
    val voteCount: Int,
    val plot: String,
    val isAdult: Boolean,
    val popularity: Int,
    @Serializable(with = PGMoneySerializer::class)
    val price: PGmoney,
    val primaryImageUrl: String,
    val isFavorite: Boolean,
    val isBought: Boolean
)
