package com.example.cinemahub.network

import com.example.cinemahub.model.api.favorite.FavoriteResponse
import com.example.cinemahub.model.api.history.HistoryResponse
import com.example.cinemahub.model.api.movie.Movie
import com.example.cinemahub.model.api.movie.MovieDetailsResponse
import com.example.cinemahub.model.api.movie.MovieSearchResponse
import com.example.cinemahub.model.api.review.ReviewResponse
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
typealias AddedMovieRequestStatus = RequestStatus<Movie>
typealias GenresMoviesRequestStatus = RequestStatus<Map<String, List<Movie>>?>
typealias MovieRequestStatus = RequestStatus<MovieDetailsResponse>

typealias ReviewsRequestStatus = RequestStatus<List<ReviewResponse>>
typealias UserReviewRequestStatus = RequestStatus<ReviewResponse?>

//typealias TransactionRequestStatus = RequestStatus<Transaction?>

typealias UserRequestStatus = RequestStatus<User>
typealias UpdateUserRequestStatus = RequestStatus<User>

typealias TokenRequestStatus = RequestStatus<Token>

typealias FavoritesRequestStatus = RequestStatus<List<FavoriteResponse>>
typealias HistoryRequestStatus = RequestStatus<List<HistoryResponse>>

typealias SearchRequestStatus = RequestStatus<List<MovieSearchResponse>>
typealias SignUpRequestStatus = RequestStatus<Token>

typealias UsersRequestStatus = RequestStatus<List<User>>
