package com.example.cinemahub.data

import com.example.cinemahub.model.api.movie.Movie
import com.example.cinemahub.model.api.user.User
import com.example.cinemahub.network.CinemaHubApiService
import javax.inject.Inject

interface CinemaHubRepository {
    suspend fun getAllMovies(): List<Movie>
    suspend fun getUserById(userId: Int) : User
}

class NetworkCinemaHubRepository @Inject constructor(
    private val cinemaHubApiService: CinemaHubApiService
) :CinemaHubRepository {
    override suspend fun getAllMovies(): List<Movie> {
        val r = cinemaHubApiService.getAllMovies("Bearer $key")
        return r.items
    }

    override suspend fun getUserById(userId: Int): User {
        val r = cinemaHubApiService.getUserById(userId = userId, authHeader = "Bearer $key")
        return r
    }
}
