package com.example.cinemahub.network

import com.example.cinemahub.model.api.favorite.FavoriteResponse
import com.example.cinemahub.model.api.movie.Movie
import com.example.cinemahub.model.api.movie.MovieSearchResponse
import com.example.cinemahub.model.api.user.Token
import com.example.cinemahub.model.api.user.User

sealed interface RequestStatus<T> {
    data class Success<T>(
        val data: T
    ) : RequestStatus<T>
    data class Error<T>(
        val exception: Exception
    ) : RequestStatus<T>
    class Loading<T> : RequestStatus<T>
}

typealias MoviesRequestStatus = RequestStatus<List<Movie>>
typealias UserRequestStatus = RequestStatus<User>
typealias UpdateUserRequestStatus = RequestStatus<User>
typealias TokenRequestStatus = RequestStatus<Token>
typealias FavoritesRequestStatus = RequestStatus<List<FavoriteResponse>>
typealias SearchRequestStatus = RequestStatus<List<MovieSearchResponse>>
//typealias HistoryRequestStatus = RequestStatus<List<HistoryResponse>>
typealias SignUpRequestStatus = RequestStatus<Token>
