package com.example.devops.ui.features.auth.data.repository

import com.example.devops.ui.features.auth.data.mapper.toDomain
import com.example.devops.ui.features.auth.data.remote.api.GitHubAuthApi
import com.example.devops.ui.features.auth.data.remote.dto.OAuthCallbackDto
import com.example.devops.ui.features.auth.domain.model.AuthResult
import com.example.devops.ui.features.auth.domain.model.AuthState
import com.example.devops.ui.features.auth.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val remoteDataSource: GitHubAuthApi
) : AuthRepository {

    private val _authState = MutableStateFlow(AuthState())
    
    override suspend fun initiateGitHubOAuth(): AuthResult<String> {
        return try {
            val response = remoteDataSource.initiateOAuth()
            if (response.isSuccessful && response.body() != null) {
                AuthResult.Success(response.body()!!.auth_url)
            } else {
                AuthResult.Error("Failed to initiate OAuth: ${response.message()}")
            }
        } catch (e: Exception) {
            AuthResult.Error("Network error: ${e.message}", e)
        }
    }

    override suspend fun handleOAuthCallback(code: String, state: String): AuthResult<AuthState> {
        return try {
            val request = OAuthCallbackDto(code = code, state = state)
            val response = remoteDataSource.handleCallback(request)
            
            if (response.isSuccessful && response.body() != null) {
                val authState = response.body()!!.toDomain()
                _authState.value = authState
                AuthResult.Success(authState)
            } else {
                AuthResult.Error("OAuth callback failed: ${response.message()}")
            }
        } catch (e: Exception) {
            AuthResult.Error("OAuth callback error: ${e.message}", e)
        }
    }

    override suspend fun getCurrentAuthState(): AuthResult<AuthState> {
        return AuthResult.Success(_authState.value)
    }

    override suspend fun refreshTokens(): AuthResult<AuthState> {
        return AuthResult.Error("Token refresh not implemented")
    }

    override suspend fun logout(): AuthResult<Unit> {
        return try {
            val response = remoteDataSource.logout()
            if (response.isSuccessful) {
                _authState.value = AuthState()
                AuthResult.Success(Unit)
            } else {
                AuthResult.Error("Logout failed: ${response.message()}")
            }
        } catch (e: Exception) {
            _authState.value = AuthState()
            AuthResult.Error("Logout error: ${e.message}", e)
        }
    }

    override fun observeAuthState(): Flow<AuthState> {
        return _authState.asStateFlow()
    }
}