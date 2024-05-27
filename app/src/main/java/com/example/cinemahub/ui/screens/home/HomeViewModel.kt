package com.example.cinemahub.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinemahub.data.CinemaHubRepository
import com.example.cinemahub.network.MoviesRequestStatus
import com.example.cinemahub.network.RequestStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: CinemaHubRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(
        HomeScreenUiState(
            moviesRequestStatus = RequestStatus.Loading()
        )
    )

    val uiState: StateFlow<HomeScreenUiState> = _uiState

    init {
        getAllMovies()
    }

    fun getAllMovies() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update {
                it.copy(
                    moviesRequestStatus = try {
                        RequestStatus.Success(repository.getAllMovies())
                    } catch (e: Exception) {
                        RequestStatus.Error(e)
                    }
                )
            }
        }
    }
}

data class HomeScreenUiState(
    val moviesRequestStatus: MoviesRequestStatus,
)
