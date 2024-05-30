package com.example.cinemahub.ui.screens.favorites

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
        FavoritesScreenUiState(
            favoritesRequestStatus = RequestStatus.Loading(),
            userId = PreferenceManagerSingleton.getUserId()
        )
    )

    val uiState: StateFlow<FavoritesScreenUiState> = _uiState

    init {
        getFavorites(_uiState.value.userId)
    }

    fun getFavorites(userId: Int) {
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
}

data class FavoritesScreenUiState(
    val favoritesRequestStatus: FavoritesRequestStatus,
    val userId: Int
)
