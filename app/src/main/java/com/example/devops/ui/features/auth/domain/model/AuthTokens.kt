package com.example.devops.ui.features.auth.domain.model

data class AuthTokens(
    val accessToken: String,
    val refreshToken: String?,
    val expiresIn: Long?,
    val tokenType: String = "Bearer"
)