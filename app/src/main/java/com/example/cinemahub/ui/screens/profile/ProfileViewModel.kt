package com.example.cinemahub.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinemahub.PreferenceManagerSingleton
import com.example.cinemahub.data.CinemaHubRepository
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
        ProfileScreenUiState()
    )

    val uiState: StateFlow<ProfileScreenUiState> = _uiState

    init {
        updateUserid(PreferenceManagerSingleton.getUserId())
        fetchUser()
    }

    fun fetchUser(userId: Int = uiState.value.userId) {
        _uiState.update {
            it.copy(
                userRequestStatus = RequestStatus.Loading()
            )
        }

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

    private fun updateUserid(userId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update {
                it.copy(
                    userId = userId
                )
            }
        }
    }
}

data class ProfileScreenUiState(
    val userRequestStatus: UserRequestStatus = RequestStatus.Loading(),
    val userId: Int = PreferenceManagerSingleton.getUserId()
)
