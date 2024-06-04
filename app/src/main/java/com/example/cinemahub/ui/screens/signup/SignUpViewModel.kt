package com.example.cinemahub.ui.screens.signup

import android.util.Patterns
import androidx.lifecycle.ViewModel
import com.example.cinemahub.PreferenceManagerSingleton
import com.example.cinemahub.data.CinemaHubRepository
import com.example.cinemahub.model.api.signUp.SignUpRequest
import com.example.cinemahub.network.RequestStatus
import com.example.cinemahub.network.SignUpRequestStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val repository: CinemaHubRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(SignUpUiState())
    val uiState: StateFlow<SignUpUiState> = _uiState

    fun updateUsername(username: String) {
        _uiState.update {
            it.copy(
                username = username,
                isUsernameValid = isUsernameValid(username)
            )
        }
    }

    fun updatePassword(password: String) {
        _uiState.update {
            it.copy(
                password = password,
                isPasswordValid = isPasswordValid(password)
            )
        }
    }

    fun updateName(name: String) {
        _uiState.update {
            it.copy(
                name = name,
                isNameValid = isNameValid(name)
            )
        }
    }

    fun updateLastName(lastName: String) {
        _uiState.update {
            it.copy(
                lastName = lastName,
                isLastNameValid = isNameValid(lastName)
            )
        }
    }

    fun updateEmail(email: String) {
        _uiState.update {
            it.copy(
                email = email,
                isEmailValid = isEmailValid(email)
            )
        }
    }

    fun updatePhoneNumber(phoneNumber: String) {
        _uiState.update {
            it.copy(
                phoneNumber = phoneNumber,
                isPhoneNumberValid = isPhoneNumberValid(phoneNumber)
            )
        }
    }

    fun updateBirthdate(birthdate: String) {
        _uiState.update {
            it.copy(
                birthdate = birthdate,
                isBirthdateValid = isBirthdateValid(birthdate)
            )
        }
    }


    private fun isUsernameValid(username: String): Boolean {
        return username.isNotBlank()
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length >= 8
    }

    private fun isNameValid(name: String): Boolean {
        return name.isNotBlank()
    }

    private fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isPhoneNumberValid(phoneNumber: String): Boolean {
        return Patterns.PHONE.matcher(phoneNumber).matches() && phoneNumber.length >= 10
    }

    private fun isBirthdateValid(birthdate: String): Boolean {
        return try {
            LocalDate.parse(birthdate) < Clock.System.now()
                .toLocalDateTime(TimeZone.currentSystemDefault()).date
        } catch (e: Exception) {
            false
        }
    }

    private fun isFormValid(): Boolean {
        return isUsernameValid(uiState.value.username) &&
                isPasswordValid(uiState.value.password) &&
                isNameValid(uiState.value.name) &&
                isNameValid(uiState.value.lastName) &&
                isEmailValid(uiState.value.email) &&
                isPhoneNumberValid(uiState.value.phoneNumber) &&
                isBirthdateValid(uiState.value.birthdate)
    }


    suspend fun signUp(): Boolean {
        if (!isFormValid()) {
            _uiState.update {
                it.copy(
                    signUpRequestStatus = RequestStatus.Error(Exception()),
                    isUsernameValid = isUsernameValid(uiState.value.username),
                    isPasswordValid = isPasswordValid(uiState.value.password),
                    isNameValid = isNameValid(uiState.value.name),
                    isLastNameValid = isNameValid(uiState.value.lastName),
                    isEmailValid = isEmailValid(uiState.value.email),
                    isPhoneNumberValid = isPhoneNumberValid(uiState.value.phoneNumber),
                    isBirthdateValid = isBirthdateValid(uiState.value.birthdate)
                )
            }
            return false
        }
        val status = withContext(Dispatchers.IO) {
            try {
                val token = repository.signUp(
                    uiState.value.toSignUpRequest()
                )
                RequestStatus.Success(token)
            } catch (e: Exception) {
                RequestStatus.Error(e)
            }
        }

        if (status is RequestStatus.Success) {
            val token = status.data.token
            PreferenceManagerSingleton.saveToken(token)
            repository.updateToken(token)
            PreferenceManagerSingleton.saveUsername(uiState.value.username)

            val userId = repository.getUserByUsername(uiState.value.username).userId
            PreferenceManagerSingleton.saveUserId(userId)
            return true
        }
        return false
    }
}

data class SignUpUiState(
    val username: String = "",
    val password: String = "",
    val name: String = "",
    val lastName: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val birthdate: String = "",

    val isUsernameValid: Boolean = true,
    val isPasswordValid: Boolean = true,
    val isNameValid: Boolean = true,
    val isLastNameValid: Boolean = true,
    val isEmailValid: Boolean = true,
    val isPhoneNumberValid: Boolean = true,
    val isBirthdateValid: Boolean = true,

    val signUpRequestStatus: SignUpRequestStatus = RequestStatus.Loading()
)

fun SignUpUiState.toSignUpRequest(): SignUpRequest = SignUpRequest(
    username = this.username,
    firstName = this.name,
    lastName = this.lastName,
    email = this.email,
    phoneNumber = this.phoneNumber,
    birthDate = LocalDate.parse(this.birthdate),
    password = this.password,
)
