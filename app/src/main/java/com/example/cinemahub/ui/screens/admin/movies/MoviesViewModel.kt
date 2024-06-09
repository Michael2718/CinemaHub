package com.example.cinemahub.ui.screens.admin.movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinemahub.data.CinemaHubRepository
import com.example.cinemahub.network.MoviesRequestStatus
import com.example.cinemahub.network.RequestStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val repository: CinemaHubRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(
        MoviesUiState()
    )

    val uiState: StateFlow<MoviesUiState> = _uiState

    init {
        fetchMovies()
    }

    fun fetchMovies() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update {
                it.copy(
                    moviesRequestStatus = try {
                        RequestStatus.Success(repository.getAllMovies(uiState.value.query))
                    } catch (e: Exception) {
                        RequestStatus.Error(e)
                    }
                )
            }
        }
    }

    fun deleteMovie(movieId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteMovie(movieId)
            fetchMovies()
        }
    }

    fun updateQuery(query: String) {
        _uiState.update {
            it.copy(
                query = query
            )
        }
    }

    fun setSearching(isSearching: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            delay(100)
            _uiState.update {
                it.copy(
                    isSearching = isSearching
                )
            }
        }
    }
}

data class MoviesUiState(
    val moviesRequestStatus: MoviesRequestStatus = RequestStatus.Loading(),
    val query: String = "",
    val isSearching: Boolean = false
)