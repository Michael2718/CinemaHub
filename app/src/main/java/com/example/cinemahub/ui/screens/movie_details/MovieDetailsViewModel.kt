package com.example.cinemahub.ui.screens.movie_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinemahub.data.CinemaHubRepository
import com.example.cinemahub.network.MovieRequestStatus
import com.example.cinemahub.network.RequestStatus
import com.example.cinemahub.network.ReviewsRequestStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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

    fun fetchMovie(movieId: String = uiState.value.movieId) {
        _uiState.update {
            it.copy(
                movieRequestStatus = RequestStatus.Loading()
            )
        }

        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update {
                it.copy(
                    movieRequestStatus = try {
                        RequestStatus.Success(
                            repository.getMovieById(movieId)
                        )
                    } catch (e: Exception) {
                        RequestStatus.Error(e)
                    }
                )
            }
        }
    }

    fun fetchReviews(movieId: String = uiState.value.movieId) {
        _uiState.update {
            it.copy(
                reviewsRequestStatus = RequestStatus.Loading()
            )
        }

        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update {
                it.copy(
                    reviewsRequestStatus = try {
                        RequestStatus.Success(
                            repository.getReviewsByMovieId(movieId)
                        )
                    } catch (e: Exception) {
                        RequestStatus.Error(e)
                    }
                )
            }
        }
    }
}

data class MovieDetailsUiState(
    val movieId: String = "",
    val movieRequestStatus: MovieRequestStatus = RequestStatus.Loading(),
    val reviewsRequestStatus: ReviewsRequestStatus = RequestStatus.Loading()
)
