package com.example.cinemahub.data

import com.example.cinemahub.PreferenceManagerSingleton
import com.example.cinemahub.model.api.favorite.FavoriteResponse
import com.example.cinemahub.model.api.history.HistoryResponse
import com.example.cinemahub.model.api.movie.Movie
import com.example.cinemahub.model.api.user.Token
import com.example.cinemahub.model.api.user.User
import com.example.cinemahub.network.CinemaHubApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext

interface CinemaHubRepository {
    suspend fun updateToken(newToken: String)

    suspend fun getAllMovies(): List<Movie>
    suspend fun getMovieById(movieId: String): Movie

    suspend fun getFavorites(userId: Int): List<FavoriteResponse>
    suspend fun deleteFavorite(userId: Int, movieId: String): Boolean
    suspend fun deleteAllFavorites(userId: Int): Boolean

    suspend fun getHistory(userId: Int): List<HistoryResponse>

    suspend fun getUserById(userId: Int): User
    suspend fun getUserByUsername(username: String): User

    suspend fun login(username: String, password: String): Token
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

    override suspend fun login(username: String, password: String): Token {
        return cinemaHubApiService.login(mapOf("username" to username, "password" to password))
    }
}

data class AuthHeader(
    val token: String = PreferenceManagerSingleton.getToken()
)

//fun AuthHeader.tokenString(): String {
//    return "Bearer ${this.token}"
//}
