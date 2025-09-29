package com.example.devops.ui.features.auth.data.local

import com.example.devops.ui.features.auth.domain.model.AuthState
import kotlinx.coroutines.flow.Flow

interface LocalAuthDataSource {
    suspend fun saveAuthState(authState: AuthState)
    suspend fun getAuthState(): AuthState?
    suspend fun clearAuthState()
    fun observeAuthState(): Flow<AuthState>
}