package com.example.cinemahub.ui.screens.signin

import androidx.lifecycle.ViewModel
import com.example.cinemahub.PreferenceManagerSingleton
import com.example.cinemahub.data.CinemaHubRepository
import com.example.cinemahub.model.api.signIn.SignInRequest
import com.example.cinemahub.network.RequestStatus
import com.example.cinemahub.network.TokenRequestStatus
import com.example.cinemahub.util.extractAudienceFromJwt
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

    suspend fun signIn(): Boolean {
        val username = _uiState.value.username
        val password = _uiState.value.password

        val status = withContext(Dispatchers.IO) {
            try {
                val token = repository.signIn(SignInRequest(username, password))
                RequestStatus.Success(token)
            } catch (e: Exception) {
                RequestStatus.Error(e)
            }
        }

        _uiState.update {
            it.copy(
                tokenRequestStatus = status
            )
        }

        if (status is RequestStatus.Success) {
            val token = status.data.token
            val audience = extractAudienceFromJwt(token)!!

            repository.updateToken(token)

            PreferenceManagerSingleton.saveToken(token)
            PreferenceManagerSingleton.saveAudience(audience)
            PreferenceManagerSingleton.saveUsername(username)

            if (audience != "admin") {
                val userId = repository.getUserByUsername(username).userId
                PreferenceManagerSingleton.saveUserId(userId)
            }
            return true
        }

        return false
    }


//    private fun getToken(): String? =
//        when (val requestStatus = _uiState.value.tokenRequestStatus) {
//            is RequestStatus.Error -> null
//            is RequestStatus.Loading -> null
//            is RequestStatus.Success -> requestStatus.data.token
//        }

    fun isLoggedIn(): Boolean {
        val token = PreferenceManagerSingleton.getToken()
        return token.isNotEmpty()
    }
}

data class SignInUiState(
    val username: String = "",
    val password: String = "",
    val tokenRequestStatus: TokenRequestStatus = RequestStatus.Loading()
)
