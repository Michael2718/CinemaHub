package com.example.cinemahub.model.api.review

import kotlinx.serialization.Serializable

@Serializable
data class ReviewResponse(
    val movieId: String,
    val userId: Int,
    val username: String,
    val vote: Int,
    val comment: String,
    val likes: Int,
    val dislikes: Int,
)

data class AddReviewRequest(
    val movieId: String,
    val userId: Int,
    val vote: Int,
    val comment: String
)
