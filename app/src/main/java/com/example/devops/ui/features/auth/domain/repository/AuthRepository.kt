package com.example.devops.ui.features.auth.domain.repository

import com.example.devops.ui.features.auth.domain.model.AuthResult
import com.example.devops.ui.features.auth.domain.model.AuthState
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun initiateGitHubOAuth(): AuthResult<String>
    suspend fun handleOAuthCallback(code: String, state: String): AuthResult<AuthState>
    suspend fun getCurrentAuthState(): AuthResult<AuthState>
    suspend fun refreshTokens(): AuthResult<AuthState>
    suspend fun logout(): AuthResult<Unit>
    fun observeAuthState(): Flow<AuthState>
}