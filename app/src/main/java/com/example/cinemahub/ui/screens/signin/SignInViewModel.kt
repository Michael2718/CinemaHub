package com.example.cinemahub.ui.screens.signin

import androidx.lifecycle.ViewModel
import com.example.cinemahub.PreferenceManagerSingleton
import com.example.cinemahub.data.CinemaHubRepository
import com.example.cinemahub.network.RequestStatus
import com.example.cinemahub.network.TokenRequestStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val repository: CinemaHubRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignInUiState())
    val uiState: StateFlow<SignInUiState> = _uiState

    fun updateUsername(username: String) {
        _uiState.update {
            it.copy(
                username = username
            )
        }
    }

    fun updatePassword(password: String) {
        _uiState.update {
            it.copy(
                password = password
            )
        }
    }

    suspend fun login(): Boolean {
        return withContext(Dispatchers.IO) {
            val status = try {
                val token = repository.login(_uiState.value.username, _uiState.value.password)
                RequestStatus.Success(token)
            } catch (e: Exception) {
                RequestStatus.Error(e)
            }

            _uiState.update {
                it.copy(tokenRequestStatus = status)
            }

            if (status is RequestStatus.Success) {
                PreferenceManagerSingleton.saveToken(getToken())
                PreferenceManagerSingleton.saveUsername(getUsername())
                true
            } else {
                false
            }
        }
    }

    private fun getToken(): String? {
        return when (val requestStatus = _uiState.value.tokenRequestStatus) {
            is RequestStatus.Error -> null
            is RequestStatus.Loading -> null
            is RequestStatus.Success -> requestStatus.data.token
        }
    }

    private fun getUsername(): String {
        return _uiState.value.username
    }

    fun isLoggedIn(): Boolean {
        val token = PreferenceManagerSingleton.getToken()
        return !token.isNullOrEmpty()
    }
}

data class SignInUiState(
    val username: String = "",
    val password: String = "",
    val tokenRequestStatus: TokenRequestStatus = RequestStatus.Loading()
)
