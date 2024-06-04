package com.example.cinemahub.model.api.signUp

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class SignUpRequest(
    val username: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phoneNumber: String,
    val birthDate: LocalDate,
    val password: String
)
