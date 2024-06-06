package com.example.cinemahub.ui.screens.movie_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinemahub.PreferenceManagerSingleton
import com.example.cinemahub.data.CinemaHubRepository
import com.example.cinemahub.model.api.review.AddReviewRequest
import com.example.cinemahub.network.MovieRequestStatus
import com.example.cinemahub.network.RequestStatus
import com.example.cinemahub.network.ReviewsRequestStatus
import com.example.cinemahub.network.UserReviewRequestStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
//    movieId: String,
    private val repository: CinemaHubRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(
        MovieDetailsUiState()
    )

    val uiState: StateFlow<MovieDetailsUiState> = _uiState

    init {
        updateMovieId(savedStateHandle["movieId"] ?: "")
        fetchMovie()
        fetchReviews()
    }

    private fun updateMovieId(movieId: String) {
        _uiState.update {
            it.copy(
                movieId = movieId
            )
        }
    }

    fun fetchMovie(
        movieId: String = uiState.value.movieId,
        userId: Int = uiState.value.userId
    ) {
        _uiState.update {
            it.copy(
                movieRequestStatus = RequestStatus.Loading()
            )
        }

        viewModelScope.launch(Dispatchers.IO) {
            delay(500)
            _uiState.update {
                it.copy(
                    movieRequestStatus = try {
                        RequestStatus.Success(
                            repository.getMovieByUserId(movieId, userId)
                        )
                    } catch (e: Exception) {
                        RequestStatus.Error(e)
                    }
                )
            }
        }
    }

    fun fetchReviews(
        movieId: String = uiState.value.movieId,
        userId: Int = uiState.value.userId
    ) {
        _uiState.update {
            it.copy(
                reviewsRequestStatus = RequestStatus.Loading(),
                userReviewRequestStatus = RequestStatus.Loading()
            )
        }

        viewModelScope.launch(Dispatchers.IO) {
            delay(500)
            _uiState.update {
                it.copy(
                    reviewsRequestStatus = try {
                        RequestStatus.Success(
                            repository.getReviewsByMovieId(movieId)
                        )
                    } catch (e: Exception) {
                        RequestStatus.Error(e)
                    },
                    userReviewRequestStatus = try {
                        RequestStatus.Success(
                            repository.getReview(movieId, userId)
                        )
                    } catch (e: Exception) {
                        RequestStatus.Error(e)
                    }
                )
            }
        }
    }

    fun likeReview(movieId: String, userId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.rateReview(movieId, userId, true)
        }
    }

    fun dislikeReview(movieId: String, userId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.rateReview(movieId, userId, false)
        }
    }

    fun addReview(vote: Int, comment: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update {
                it.copy(
                    userReviewRequestStatus = try {
                        RequestStatus.Success(
                            repository.addReview(
                                AddReviewRequest(
                                    uiState.value.movieId,
                                    uiState.value.userId,
                                    vote,
                                    comment
                                )
                            )
                        )
                    } catch (e: Exception) {
                        RequestStatus.Error(e)
                    }
                )
            }
        }
    }

    fun onFavoriteClick(
        isFavorite: Boolean,
        movieId: String = uiState.value.movieId,
        userId: Int = uiState.value.userId
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            if (isFavorite) {
                repository.deleteFavorite(
                    userId = userId,
                    movieId = movieId
                )
            } else {
                repository.addFavorite(
                    userId = userId,
                    movieId = movieId
                )
            }
        }
    }

    fun onBuyClick(
        movieId: String = uiState.value.movieId,
        userId: Int = uiState.value.userId
    ) {
//        _uiState.update {
//            it.copy(
//                transactionRequestStatus = RequestStatus.Loading()
//            )
//        }
        viewModelScope.launch(Dispatchers.IO) {
            repository.buyMovie(
                movieId = movieId,
                userId = userId,
                paymentMethod = 1
            )
        }
    }
}

data class MovieDetailsUiState(
    val movieId: String = "",
    val userId: Int = PreferenceManagerSingleton.getUserId(),
    val movieRequestStatus: MovieRequestStatus = RequestStatus.Loading(),
    val reviewsRequestStatus: ReviewsRequestStatus = RequestStatus.Loading(),
    val userReviewRequestStatus: UserReviewRequestStatus = RequestStatus.Loading(),
//    val transactionRequestStatus: TransactionRequestStatus = RequestStatus.Loading()
)
