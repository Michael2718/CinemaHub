package com.example.cinemahub.model.api.transaction

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class Transaction(
    val userId: Int,
    val movieId: String,
    val purchaseDate: LocalDateTime,
    val paymentMethod: Int
)
