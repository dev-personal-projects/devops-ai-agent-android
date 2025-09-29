package com.example.devops.ui.features.auth.data.remote.dto

data class AuthResponseDto(
    val user: UserDto,
    val tokens: AuthTokensDto
)

data class AuthTokensDto(
    val accessToken: String,
    val refreshToken: String?,
    val expiresIn: Long?,
    val tokenType: String = "Bearer"
)