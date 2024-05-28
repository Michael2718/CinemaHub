package com.example.cinemahub.data

import com.example.cinemahub.model.api.movie.Movie
import com.example.cinemahub.model.api.user.Token
import com.example.cinemahub.model.api.user.User
import com.example.cinemahub.network.CinemaHubApiService

interface CinemaHubRepository {
    suspend fun getAllMovies(): List<Movie>
    suspend fun getUserById(userId: Int): User
    suspend fun login(username: String, password: String): Token
}

class NetworkCinemaHubRepository(
    private val token: String?,
    private val cinemaHubApiService: CinemaHubApiService,
) : CinemaHubRepository {


    override suspend fun getAllMovies(): List<Movie> {
        return cinemaHubApiService.getAllMovies("Bearer $token").items
    }

    override suspend fun getUserById(userId: Int): User {
        return cinemaHubApiService.getUserById(userId = userId, authHeader = "Bearer $token")
    }

    override suspend fun login(username: String, password: String): Token {
        return cinemaHubApiService.login(mapOf("username" to username, "password" to password))
    }
}
