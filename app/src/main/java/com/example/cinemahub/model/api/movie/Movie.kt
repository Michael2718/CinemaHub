package com.example.cinemahub.model.api.movie

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class Movie(
    val movieId: String,
    val title: String,
    val releaseDate: LocalDate,
//    @Serializable(with = PGIntervalSerializer::class)
//    val duration: PGInterval,
    val voteAverage: Double,
    val voteCount: Int,
    val plot: String,
    val isAdult: Boolean,
    val popularity: Int,
//    @Serializable(with = PGMoneySerializer::class)
//    val price: PGmoney,
    val primaryImageUrl: String
)
