package com.example.cinemahub.ui.screens.user.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinemahub.PreferenceManagerSingleton
import com.example.cinemahub.data.CinemaHubRepository
import com.example.cinemahub.network.FavoritesRequestStatus
import com.example.cinemahub.network.RequestStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val repository: CinemaHubRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(
        FavoritesScreenUiState()
    )

    val uiState: StateFlow<FavoritesScreenUiState> = _uiState

    init {
        fetchFavorites()
    }

    fun fetchFavorites(userId: Int = uiState.value.userId) {
        _uiState.update {
            it.copy(
                favoritesRequestStatus = RequestStatus.Loading()
            )
        }

        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update {
                it.copy(
                    favoritesRequestStatus = try {
                        RequestStatus.Success(repository.getFavorites(userId))
                    } catch (e: Exception) {
                        RequestStatus.Error(e)
                    }
                )
            }
        }
    }

    fun deleteFavorite(movieId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteFavorite(uiState.value.userId, movieId)
            fetchFavorites(uiState.value.userId)
        }
    }
}

data class FavoritesScreenUiState(
    val favoritesRequestStatus: FavoritesRequestStatus = RequestStatus.Loading(),
    val userId: Int = PreferenceManagerSingleton.getUserId()
)
