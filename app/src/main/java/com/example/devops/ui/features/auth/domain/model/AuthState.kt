package com.example.devops.ui.features.auth.domain.model

data class AuthState(
    val isAuthenticated: Boolean = false,
    val user: User? = null,
    val tokens: AuthTokens? = null
)