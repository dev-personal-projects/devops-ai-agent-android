package com.example.devops.ui.features.auth.data.local.model

import com.example.devops.ui.features.auth.domain.model.AuthTokens

data class AuthTokensStorageModel(
    val accessToken: String,
    val refreshToken: String?,
    val expiresIn: Long?,
    val tokenType: String
) {
    fun toDomain(): AuthTokens = AuthTokens(
        accessToken = accessToken,
        refreshToken = refreshToken,
        expiresIn = expiresIn,
        tokenType = tokenType
    )
}

fun AuthTokens.toStorageModel(): AuthTokensStorageModel = AuthTokensStorageModel(
    accessToken = accessToken,
    refreshToken = refreshToken,
    expiresIn = expiresIn,
    tokenType = tokenType
)