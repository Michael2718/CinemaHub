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
        ProfileScreenUiState(
            username = PreferenceManagerSingleton.getUsername()
        )
    )

    val uiState: StateFlow<ProfileScreenUiState> = _uiState

    init {
        fetchUser(_uiState.value.username ?: "")
    }

    fun fetchUser(username: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update {
                it.copy(
                    userRequestStatus = try {
                        RequestStatus.Success(
                            repository.getUserByUsername(username)
                        )
                    } catch (e: Exception) {
                        RequestStatus.Error(e)
                    }
                )
            }
        }
    }

//    fun updateUser(username: String) {
//        viewModelScope.launch(Dispatchers.IO) {
//            _uiState.update {
//                it.copy(
//                    username = username
//                )
//            }
//        }
//    }
}

data class ProfileScreenUiState(
    val userRequestStatus: UserRequestStatus = RequestStatus.Loading(),
    val username: String?
)
