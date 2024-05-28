package com.example.cinemahub.network

import com.example.cinemahub.model.api.movie.Movie
import com.example.cinemahub.model.api.user.Token
import com.example.cinemahub.model.api.user.User
import java.lang.Exception

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
typealias TokenRequestStatus = RequestStatus<Token>
