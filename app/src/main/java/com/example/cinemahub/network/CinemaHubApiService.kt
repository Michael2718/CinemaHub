package com.example.cinemahub.network

import com.example.cinemahub.model.api.favorite.FavoriteRequest
import com.example.cinemahub.model.api.favorite.FavoriteResponse
import com.example.cinemahub.model.api.history.HistoryResponse
import com.example.cinemahub.model.api.movie.Movie
import com.example.cinemahub.model.api.movie.MovieSearchResponse
import com.example.cinemahub.model.api.movie.MoviesResponse
import com.example.cinemahub.model.api.signIn.SignInRequest
import com.example.cinemahub.model.api.signUp.SignUpRequest
import com.example.cinemahub.model.api.user.Token
import com.example.cinemahub.model.api.user.UpdateUserRequest
import com.example.cinemahub.model.api.user.User
import kotlinx.datetime.LocalDate
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
        @Query("minDuration") minDuration: String?,
        @Query("maxDuration") maxDuration: String?,
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

    @POST("favorites")
    suspend fun addFavorite(
        @Body favoriteRequest: FavoriteRequest,
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

    @POST("user/{userId}")
    suspend fun updateUser(
        @Path("userId") userId: Int,
        @Body updateUserRequestStatus: UpdateUserRequest,
        @Header("Authorization") authHeader: String
    ): User

    @POST("signin")
    suspend fun signIn(@Body credentials: SignInRequest): Token

    @POST("signup")
    suspend fun signUp(@Body signUpRequest: SignUpRequest): Token
}
