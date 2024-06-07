package com.example.cinemahub.model.api.history

import com.example.cinemahub.model.api.movie.Movie
import com.example.cinemahub.types.PGIntervalSerializer
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import org.postgresql.util.PGInterval

//@Serializable
//data class History(
//    val userId: Int,
//    val movieId: String,
//    val watchedDate: LocalDateTime
////    val watchedDuration: PGInterval
//)

@Serializable
data class HistoryResponse(
    val movie: Movie,
    val watchedDate: LocalDateTime,
    @Serializable(with = PGIntervalSerializer::class)
    val watchedDuration: PGInterval
)
