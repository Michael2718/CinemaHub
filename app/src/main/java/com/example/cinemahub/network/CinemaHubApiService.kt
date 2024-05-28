package com.example.cinemahub.network

import com.example.cinemahub.model.api.movie.MoviesResponse
import com.example.cinemahub.model.api.user.Token
import com.example.cinemahub.model.api.user.User
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface CinemaHubApiService {
    // Movie
    @GET("movie")
    suspend fun getAllMovies(@Header("Authorization") authHeader: String): MoviesResponse

    @GET("user/{userId}")
    suspend fun getUserById(@Path("userId") userId: Int, @Header("Authorization") authHeader: String) : User

    @POST("login")
    suspend fun login(@Body credentials: Map<String, String>) : Token
}
