package com.example.devops.ui.features.auth.data.mapper

import com.example.devops.ui.features.auth.data.remote.dto.AuthResponseDto
import com.example.devops.ui.features.auth.domain.model.AuthState
import com.example.devops.ui.features.auth.domain.model.AuthTokens

fun AuthResponseDto.toDomain(): AuthState {
    return AuthState(
        isAuthenticated = true,
        user = user.toDomain(),
        tokens = AuthTokens(
            accessToken = tokens.accessToken,
            refreshToken = tokens.refreshToken,
            expiresIn = tokens.expiresIn,
            tokenType = tokens.tokenType
        )
    )
}