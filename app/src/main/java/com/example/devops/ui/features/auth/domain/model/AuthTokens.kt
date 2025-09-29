package com.example.devops.ui.features.auth.domain.model

data class AuthTokens(
    val accessToken: String,
    val refreshToken: String? = null,
    val expiresIn: Long? = null,
    val tokenType: String = "Bearer"
)