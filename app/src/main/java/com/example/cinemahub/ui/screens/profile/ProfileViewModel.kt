package com.example.cinemahub.ui.screens.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinemahub.data.CinemaHubRepository
import com.example.cinemahub.network.MoviesRequestStatus
import com.example.cinemahub.network.RequestStatus
import com.example.cinemahub.network.UserRequestStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: CinemaHubRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(
        ProfileScreenUiState(
            userRequestStatus = RequestStatus.Loading()
        )
    )

    val uiState: StateFlow<ProfileScreenUiState> = _uiState

    init {
//        fetchUser(userId: Int)
        fetchUser(2)
    }

    fun fetchUser(userId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update {
                it.copy(
                    userRequestStatus = try {
                        RequestStatus.Success(
                            repository.getUserById(userId)
                        )
                    } catch (e: Exception) {
                        RequestStatus.Error(e)
                    }
                )
            }
        }
    }
}

data class ProfileScreenUiState(
    val userRequestStatus: UserRequestStatus,
)

