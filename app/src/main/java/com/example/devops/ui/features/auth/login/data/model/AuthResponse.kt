package com.example.devops.ui.features.auth.login.data.model

data class AuthResponse(
    val access_token: String,
    val user: GitHubUser,
    val expires_in: Long? = null
)