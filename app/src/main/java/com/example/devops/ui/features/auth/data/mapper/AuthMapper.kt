package com.example.devops.ui.features.auth.data.mapper

import com.example.devops.ui.features.auth.data.remote.dto.AuthResponseDto
import com.example.devops.ui.features.auth.data.remote.dto.UserDto
import com.example.devops.ui.features.auth.data.remote.dto.AuthTokensDto
import com.example.devops.ui.features.auth.domain.model.AuthState
import com.example.devops.ui.features.auth.domain.model.User
import com.example.devops.ui.features.auth.domain.model.AuthTokens

fun AuthResponseDto.toDomain(): AuthState {
    return AuthState(
        isAuthenticated = true,
        user = user.toDomain(),
        tokens = tokens.toDomain()
    )
}



fun AuthTokensDto.toDomain(): AuthTokens {
    return AuthTokens(
        accessToken = accessToken,
        refreshToken = refreshToken,
        expiresIn = expiresIn,
        tokenType = tokenType
    )
}