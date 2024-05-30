package com.example.cinemahub.network

import com.example.cinemahub.model.api.favorite.FavoriteResponse
import com.example.cinemahub.model.api.history.HistoryResponse
import com.example.cinemahub.model.api.movie.Movie
import com.example.cinemahub.model.api.movie.MoviesResponse
import com.example.cinemahub.model.api.user.Token
import com.example.cinemahub.model.api.user.User
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface CinemaHubApiService {
    // Movie
    @GET("movie")
    suspend fun getAllMovies(@Header("Authorization") authHeader: String): MoviesResponse

    @GET("movie/{movieId}")
    suspend fun getMovieById(@Path("movieId") movieId: String, @Header("Authorization") authHeader: String): Movie

    @GET("favorites/{userId}")
    suspend fun getFavorites(@Path("userId") userId: Int, @Header("Authorization") authHeader: String): List<FavoriteResponse>

    @GET("history/{userId}")
    suspend fun getHistory(@Path("userId") userId: Int, @Header("Authorization") authHeader: String): List<HistoryResponse>

    @GET("user/{userId}")
    suspend fun getUserById(@Path("userId") userId: Int, @Header("Authorization") authHeader: String) : User
    @GET("user")
    suspend fun getUserByUsername(@Query("username") username: String, @Header("Authorization") authHeader: String) : User
    @POST("login")
    suspend fun login(@Body credentials: Map<String, String>) : Token
}
