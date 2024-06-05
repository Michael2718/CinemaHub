package com.example.cinemahub.data

import com.example.cinemahub.PreferenceManagerSingleton
import com.example.cinemahub.model.api.favorite.FavoriteRequest
import com.example.cinemahub.model.api.favorite.FavoriteResponse
import com.example.cinemahub.model.api.history.HistoryResponse
import com.example.cinemahub.model.api.movie.Movie
import com.example.cinemahub.model.api.movie.MovieSearchResponse
import com.example.cinemahub.model.api.signIn.SignInRequest
import com.example.cinemahub.model.api.signUp.SignUpRequest
import com.example.cinemahub.model.api.user.Token
import com.example.cinemahub.model.api.user.UpdateUserRequest
import com.example.cinemahub.model.api.user.User
import com.example.cinemahub.network.CinemaHubApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDate
import org.postgresql.util.PGInterval

interface CinemaHubRepository {
    suspend fun updateToken(newToken: String)

    suspend fun getAllMovies(): List<Movie>
    suspend fun getMovieById(movieId: String): Movie
    suspend fun searchMovies(
        query: String?,
        minVoteAverage: Double? = null,
        maxVoteAverage: Double? = null,
        minReleaseDate: LocalDate? = null,
        maxReleaseDate: LocalDate? = null,
        minDuration: PGInterval? = null,
        maxDuration: PGInterval? = null,
        minPrice: Double? = null,
        maxPrice: Double? = null,
        isAdult: Boolean? = null,
        userId: Int?
    ): List<MovieSearchResponse>

    suspend fun getFavorites(userId: Int): List<FavoriteResponse>
    suspend fun deleteFavorite(userId: Int, movieId: String): Boolean
    suspend fun deleteAllFavorites(userId: Int): Boolean
    suspend fun addFavorite(userId: Int, movieId: String): Boolean

    suspend fun getHistory(userId: Int): List<HistoryResponse>

    suspend fun getUserById(userId: Int): User
    suspend fun getUserByUsername(username: String): User
    suspend fun updateUser(userId: Int, updateUserRequest: UpdateUserRequest): User

    suspend fun signIn(signInRequest: SignInRequest): Token
    suspend fun signUp(signUpRequest: SignUpRequest): Token
}


class NetworkCinemaHubRepository(
    private val cinemaHubApiService: CinemaHubApiService,
) : CinemaHubRepository {
    private val _authHeader = MutableStateFlow(AuthHeader())
    private val authHeader: StateFlow<AuthHeader> = _authHeader

    override suspend fun updateToken(newToken: String) {
        withContext(Dispatchers.IO) {
            _authHeader.update {
                it.copy(
                    token = newToken
                )
            }
        }
    }

    private fun getToken(): String = authHeader.value.token
    private fun getHeader(): String = "Bearer ${authHeader.value.token}"

    override suspend fun getAllMovies(): List<Movie> {
        return cinemaHubApiService.getAllMovies(getToken()).items
    }

    override suspend fun getMovieById(movieId: String): Movie {
        return cinemaHubApiService.getMovieById(movieId = movieId, authHeader = getHeader())
    }

    override suspend fun searchMovies(
        query: String?,
        minVoteAverage: Double?,
        maxVoteAverage: Double?,
        minReleaseDate: LocalDate?,
        maxReleaseDate: LocalDate?,
        minDuration: PGInterval?,
        maxDuration: PGInterval?,
        minPrice: Double?,
        maxPrice: Double?,
        isAdult: Boolean?,
        userId: Int?
    ): List<MovieSearchResponse> {
        val movies = cinemaHubApiService.searchMovies(
            query = query,
            minVoteAverage = minVoteAverage,
            maxVoteAverage = maxVoteAverage,
            minReleaseDate = minReleaseDate,
            maxReleaseDate = maxReleaseDate,
            minDuration = minDuration?.toString(),
            maxDuration = maxDuration?.toString(),
            minPrice = minPrice,
            maxPrice = maxPrice,
            isAdult = isAdult,
            userId = userId,
            authHeader = getHeader()
        )
        return movies
    }

    override suspend fun getFavorites(userId: Int): List<FavoriteResponse> {
        return cinemaHubApiService.getFavorites(userId, getHeader())
    }

    override suspend fun deleteFavorite(userId: Int, movieId: String): Boolean {
        return try {
            cinemaHubApiService.deleteFavorite(userId, movieId, getHeader())
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun deleteAllFavorites(userId: Int): Boolean {
        return try {
            cinemaHubApiService.deleteAllFavorites(userId, getHeader())
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun addFavorite(userId: Int, movieId: String): Boolean {
        return try {
            cinemaHubApiService.addFavorite(FavoriteRequest(userId, movieId), getHeader())
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun getHistory(userId: Int): List<HistoryResponse> {
        return cinemaHubApiService.getHistory(userId = userId, authHeader = getHeader())
    }

    override suspend fun getUserById(userId: Int): User {
        return cinemaHubApiService.getUserById(userId = userId, authHeader = getHeader())
    }

    override suspend fun getUserByUsername(username: String): User {
        return cinemaHubApiService.getUserByUsername(
            username = username,
            authHeader = getHeader()
        )
    }

    override suspend fun updateUser(userId: Int, updateUserRequest: UpdateUserRequest): User {
        return cinemaHubApiService.updateUser(userId, updateUserRequest, getHeader())
    }

    override suspend fun signIn(signInRequest: SignInRequest): Token {
        return cinemaHubApiService.signIn(signInRequest)
    }

    override suspend fun signUp(signUpRequest: SignUpRequest): Token {
        return cinemaHubApiService.signUp(signUpRequest)
    }
}

data class AuthHeader(
    val token: String = PreferenceManagerSingleton.getToken()
)

//fun AuthHeader.tokenString(): String {
//    return "Bearer ${this.token}"
//}
