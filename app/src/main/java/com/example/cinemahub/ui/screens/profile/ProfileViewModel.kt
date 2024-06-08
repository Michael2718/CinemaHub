package com.example.cinemahub.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinemahub.PreferenceManagerSingleton
import com.example.cinemahub.data.CinemaHubRepository
import com.example.cinemahub.model.api.user.UpdateUserRequest
import com.example.cinemahub.network.RequestStatus
import com.example.cinemahub.network.UpdateUserRequestStatus
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
//        updateUserid(PreferenceManagerSingleton.getUserId())
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
            val status = uiState.value.userRequestStatus
            if (status is RequestStatus.Success) {
                updateUsername(status.data.username)
                updateName(status.data.firstName)
                updateLastName(status.data.lastName)
                updateEmail(status.data.email)
                updatePhoneNumber(status.data.phoneNumber)
                updateBirthdate(status.data.birthDate.toString())
            }
        }
    }

//    private fun updateUserid(userId: Int) {
//        viewModelScope.launch(Dispatchers.IO) {
//            _uiState.update {
//                it.copy(
//                    userId = userId
//                )
//            }
//        }
//    }

    fun updateUsername(username: String) {
        _uiState.update {
            it.copy(
                username = username
            )
        }
    }

    fun updateName(name: String) {
        _uiState.update {
            it.copy(
                firstName = name
            )
        }
    }

    fun updateLastName(lastName: String) {
        _uiState.update {
            it.copy(
                lastName = lastName
            )
        }
    }

    fun updateEmail(email: String) {
        _uiState.update {
            it.copy(
                email = email
            )
        }
    }

    private fun updatePhoneNumber(phoneNumber: String) {
        _uiState.update {
            it.copy(
                phoneNumber = phoneNumber
            )
        }
    }

    private fun updateBirthdate(birthdate: String) {
        _uiState.update {
            it.copy(
                birthdate = birthdate
            )
        }
    }

    fun setUpdateRequestStatus() {
        _uiState.update {
            it.copy(
                updateRequestStatus = RequestStatus.Loading()
            )
        }
    }

    fun updateUser() {
//        _uiState.update {
//            it.copy(
//                userRequestStatus = RequestStatus.Loading()
//            )
//        }

        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update {
                it.copy(
                    updateRequestStatus = try {
                        RequestStatus.Success(
                            repository.updateUser(
                                userId = uiState.value.userId,
                                UpdateUserRequest(
                                    username = uiState.value.username,
                                    firstName = uiState.value.firstName,
                                    lastName = uiState.value.lastName,
                                    email = uiState.value.email,
                                )
                            )
                        )
                    } catch (e: Exception) {
                        RequestStatus.Error(e)
                    },
                )
            }
            if (uiState.value.userRequestStatus is RequestStatus.Success) {
                _uiState.update {
                    it.copy(
                        userRequestStatus = try {
                            RequestStatus.Success(
                                repository.getUserById(uiState.value.userId)
                            )
                        } catch (e: Exception) {
                            RequestStatus.Error(e)
                        },
                    )
                }
            }
        }
    }
}

data class ProfileScreenUiState(
    val userRequestStatus: UserRequestStatus = RequestStatus.Loading(),
    val userId: Int = PreferenceManagerSingleton.getUserId(),
    val username: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val birthdate: String = "",
    val updateRequestStatus: UpdateUserRequestStatus = RequestStatus.Loading()
)
