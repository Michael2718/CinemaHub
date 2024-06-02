package com.example.cinemahub.network

import com.example.cinemahub.model.api.favorite.FavoriteResponse
import com.example.cinemahub.model.api.history.HistoryResponse
import com.example.cinemahub.model.api.movie.Movie
import com.example.cinemahub.model.api.movie.MovieSearchResponse
import com.example.cinemahub.model.api.movie.MoviesResponse
import com.example.cinemahub.model.api.user.Token
import com.example.cinemahub.model.api.user.User
import kotlinx.datetime.LocalDate
import org.postgresql.util.PGInterval
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface CinemaHubApiService {
    /*
    * Movies
    * */
    @GET("movie")
    suspend fun getAllMovies(@Header("Authorization") authHeader: String): MoviesResponse

    @GET("movie/{movieId}")
    suspend fun getMovieById(
        @Path("movieId") movieId: String,
        @Header("Authorization") authHeader: String
    ): Movie

    @GET("search")
    suspend fun searchMovies(
        @Query("query") query: String?,
        @Query("minVoteAverage") minVoteAverage: Double?,
        @Query("maxVoteAverage") maxVoteAverage: Double?,
        @Query("minReleaseDate") minReleaseDate: LocalDate?,
        @Query("maxReleaseDate") maxReleaseDate: LocalDate?,
        @Query("minDuration") minDuration: PGInterval?,
        @Query("maxDuration") maxDuration: PGInterval?,
        @Query("minPrice") minPrice: Double?,
        @Query("maxPrice") maxPrice: Double?,
        @Query("isAdult") isAdult: Boolean?,
        @Query("userId") userId: Int?,
        @Header("Authorization") authHeader: String
    ): List<MovieSearchResponse>

    /*
    * Favorites
    * */
    @GET("favorites/{userId}")
    suspend fun getFavorites(
        @Path("userId") userId: Int,
        @Header("Authorization") authHeader: String
    ): List<FavoriteResponse>

    @DELETE("favorites/{userId}/{movieId}")
    suspend fun deleteFavorite(
        @Path("userId") userId: Int,
        @Path("movieId") movieId: String,
        @Header("Authorization") authHeader: String
    )

    @DELETE("favorites/{userId}")
    suspend fun deleteAllFavorites(
        @Path("userId") userId: Int,
        @Header("Authorization") authHeader: String
    )

    /*
    * History
    * */
    @GET("history/{userId}")
    suspend fun getHistory(
        @Path("userId") userId: Int,
        @Header("Authorization") authHeader: String
    ): List<HistoryResponse>

    /*
    * User
    * */
    @GET("user/{userId}")
    suspend fun getUserById(
        @Path("userId") userId: Int,
        @Header("Authorization") authHeader: String
    ): User

    @GET("user")
    suspend fun getUserByUsername(
        @Query("username") username: String,
        @Header("Authorization") authHeader: String
    ): User

    @POST("login")
    suspend fun login(@Body credentials: Map<String, String>): Token
}
