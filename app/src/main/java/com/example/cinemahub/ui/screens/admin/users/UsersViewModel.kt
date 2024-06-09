package com.example.cinemahub.ui.screens.admin.users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinemahub.data.CinemaHubRepository
import com.example.cinemahub.network.RequestStatus
import com.example.cinemahub.network.UsersRequestStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(
    private val repository: CinemaHubRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(
        UsersUiState()
    )

    val uiState: StateFlow<UsersUiState> = _uiState

    init {
        fetchUsers()
    }

    fun fetchUsers() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update {
                it.copy(
                    usersRequestStatus = try {
                        RequestStatus.Success(
                            repository.getAllUsers(
                                uiState.value.query
                            )
                        )
                    } catch (e: Exception) {
                        RequestStatus.Error(e)
                    }
                )
            }
        }
    }

    fun deleteMovie(userId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteUser(userId)
            fetchUsers()
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

data class UsersUiState(
    val usersRequestStatus: UsersRequestStatus = RequestStatus.Loading(),
    val query: String = "",
    val isSearching: Boolean = false,
)
