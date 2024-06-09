package com.example.cinemahub.ui.screens.admin.movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinemahub.data.CinemaHubRepository
import com.example.cinemahub.model.api.movie.AddMovieRequest
import com.example.cinemahub.network.AddedMovieRequestStatus
import com.example.cinemahub.network.RequestStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import org.postgresql.util.PGInterval
import javax.inject.Inject

@HiltViewModel
class AddMovieViewModel @Inject constructor(
    private val repository: CinemaHubRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(
        AddMovieUiState()
    )

    val uiState: StateFlow<AddMovieUiState> = _uiState

    //    init {
//        fetchMovies()
//    }
//
//    fun fetchMovies() {
//        viewModelScope.launch(Dispatchers.IO) {
//            _uiState.update {
//                it.copy(
//                    moviesRequestStatus = try {
//                        RequestStatus.Success(repository.getAllMovies())
//                    } catch (e: Exception) {
//                        RequestStatus.Error(e)
//                    }
//                )
//            }
//        }
//    }
//
//    fun deleteMovie(movieId: String) {
//        viewModelScope.launch(Dispatchers.IO) {
//            repository.deleteMovie(movieId)
//            fetchMovies()
//        }
//    }
    fun updateMovieId(movieId: String) {
        _uiState.update {
            it.copy(
                movieId = movieId
            )
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

    fun clearUiState() {
        _uiState.update {
            AddMovieUiState()
        }
    }

    fun save() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update {
                it.copy(
                    addedMovieRequestStatus = try {
                        val movie = repository.addMovie(uiState.value.toAddMovieRequest())
                        RequestStatus.Success(movie)
                    } catch (e: Exception) {
                        RequestStatus.Error(e)
                    }
                )
            }
        }
    }
}

data class AddMovieUiState(
    val addedMovieRequestStatus: AddedMovieRequestStatus = RequestStatus.Loading(),

    val movieId: String = "",
    val title: String = "",
    val releaseDate: String = "",
    val duration: String = "",
    val plot: String = "",
    val isAdult: Boolean = false,
    val price: String = "",
    val primaryImageUrl: String = ""
)

fun AddMovieUiState.toAddMovieRequest(): AddMovieRequest = AddMovieRequest(
    movieId = this.movieId,
    title = this.title,
    releaseDate = LocalDate.parse(this.releaseDate),
    duration = parsePGInterval(this.duration), // parsePGInterval(this.duration),
    plot = this.plot,
    isAdult = this.isAdult,
    price = this.price.toDouble(),
    primaryImageUrl = this.primaryImageUrl
)

private fun parsePGInterval(intervalString: String): PGInterval {
    val pattern1 = Regex("(\\d+)h\\s?(\\d+)m")
    val pattern2 = Regex("(\\d+)\\s?hours?\\s?(\\d+)\\s?minutes?")
    var duration: Pair<Int, Int> = 0 to 0

    val match1 = pattern1.find(intervalString)
    if (match1 != null) {
        val hours = match1.groupValues[1].toInt()
        val minutes = match1.groupValues[2].toInt()
        duration = hours to minutes
    }

    val match2 = pattern2.find(intervalString)
    if (match2 != null) {
        val hours = match2.groupValues[1].toInt()
        val minutes = match2.groupValues[2].toInt()
        duration = hours to minutes
    }

    return PGInterval(0, 0, 0, duration.first, duration.second, 0.0)
}