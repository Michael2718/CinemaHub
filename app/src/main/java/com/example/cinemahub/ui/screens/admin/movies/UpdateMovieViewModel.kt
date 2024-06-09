package com.example.cinemahub.ui.screens.admin.movies

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinemahub.data.CinemaHubRepository
import com.example.cinemahub.model.api.movie.UpdateMovieRequest
import com.example.cinemahub.network.MovieRequestStatus
import com.example.cinemahub.network.RequestStatus
import com.example.cinemahub.network.UpdateMovieRequestStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import javax.inject.Inject

@HiltViewModel
class UpdateMovieViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: CinemaHubRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(
        UpdateMovieUiState(
            movieId = savedStateHandle["movieId"] ?: ""
        )
    )

    val uiState: StateFlow<UpdateMovieUiState> = _uiState

    init {
        fetchMovie()
    }

    fun fetchMovie() {
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
                            repository.getMovieById(
                                uiState.value.movieId
                            )
                        )
                    } catch (e: Exception) {
                        RequestStatus.Error(e)
                    }
                )
            }
            val status = uiState.value.movieRequestStatus
            if (status is RequestStatus.Success) {
                updateTitle(status.data.title)
                updateReleaseDate(status.data.releaseDate.toString())
                updateDuration("${status.data.duration.hours}h ${status.data.duration.minutes}m")
                updatePlot(status.data.plot)
                updateAdult(status.data.isAdult)
                updatePrice(status.data.price.`val`.toString())
                updatePrimaryImageUrl(status.data.primaryImageUrl)
            }
        }
    }

    fun updateTitle(title: String) {
        _uiState.update {
            it.copy(
                title = title
            )
        }
    }

    fun updateReleaseDate(releaseDate: String) {
        _uiState.update {
            it.copy(
                releaseDate = releaseDate
            )
        }
    }

    fun updateDuration(duration: String) {
        _uiState.update {
            it.copy(
                duration = duration
            )
        }
    }

    fun updatePlot(plot: String) {
        _uiState.update {
            it.copy(
                plot = plot
            )
        }
    }

    fun updateAdult(isAdult: Boolean) {
        _uiState.update {
            it.copy(
                isAdult = isAdult
            )
        }
    }

    fun updatePrice(price: String) {
        _uiState.update {
            it.copy(
                price = price
            )
        }
    }

    fun updatePrimaryImageUrl(primaryImageUrl: String) {
        _uiState.update {
            it.copy(
                primaryImageUrl = primaryImageUrl
            )
        }
    }

//    fun clearUiState() {
//        _uiState.update {
//            UpdateMovieUiState()
//        }
//    }

    fun save() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update {
                it.copy(
                    updateMovieRequestStatus = try {
                        RequestStatus.Success(
                            repository.updateMovie(
                                movieId = uiState.value.movieId,
                                request = uiState.value.toUpdateMovieRequest()
                            )
                        )
                    } catch (e: Exception) {
                        RequestStatus.Error(e)
                    },
                )
            }
        }
    }
}

data class UpdateMovieUiState(
    val updateMovieRequestStatus: UpdateMovieRequestStatus = RequestStatus.Loading(),
    val movieRequestStatus: MovieRequestStatus = RequestStatus.Loading(),

    val movieId: String = "",

    val title: String = "",
    val releaseDate: String = "",
    val duration: String = "",
    val plot: String = "",
    val isAdult: Boolean = false,
    val price: String = "",
    val primaryImageUrl: String = ""
)

fun UpdateMovieUiState.toUpdateMovieRequest(): UpdateMovieRequest = UpdateMovieRequest(
    title = this.title,
    releaseDate = LocalDate.parse(this.releaseDate),
    duration = parsePGInterval(this.duration), // parsePGInterval(this.duration),
    plot = this.plot,
    isAdult = this.isAdult,
    price = this.price.toDouble(),
    primaryImageUrl = this.primaryImageUrl
)