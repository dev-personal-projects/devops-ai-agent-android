package com.example.devops.ui.features.auth.data.repository

import com.example.devops.ui.features.auth.data.local.LocalAuthDataSource
import com.example.devops.ui.features.auth.data.remote.api.GitHubAuthApi
import com.example.devops.ui.features.auth.data.mapper.toDomain
import com.example.devops.ui.features.auth.domain.model.AuthResult
import com.example.devops.ui.features.auth.domain.model.AuthState
import com.example.devops.ui.features.auth.domain.repository.AuthRepository
import com.example.devops.ui.features.auth.data.remote.dto.OAuthCallbackDto
import com.example.devops.ui.features.auth.data.remote.dto.RefreshTokenDto
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val remoteDataSource: GitHubAuthApi,
    private val localDataSource: LocalAuthDataSource
) : AuthRepository {

    override suspend fun initiateGitHubOAuth(): AuthResult<String> {
        return try {
            val response = remoteDataSource.initiateOAuth()
            if (response.isSuccessful && response.body() != null) {
                AuthResult.Success(response.body()!!.auth_url)
            } else {
                AuthResult.Error(
                    Exception("HTTP ${response.code()}"),
                    "Failed to initiate OAuth: ${response.message()}"
                )
            }
        } catch (e: Exception) {
            AuthResult.Error(e, "Network error during OAuth initiation: ${e.message}")
        }
    }

    override suspend fun handleOAuthCallback(code: String, state: String): AuthResult<AuthState> {
        return try {
            val request = OAuthCallbackDto(code, state)
            val response = remoteDataSource.handleCallback(request)

            if (response.isSuccessful && response.body() != null) {
                val authState = response.body()!!.toDomain()
                localDataSource.saveAuthState(authState)
                AuthResult.Success(authState)
            } else {
                AuthResult.Error(
                    Exception("HTTP ${response.code()}"),
                    "Authentication failed: ${response.message()}"
                )
            }
        } catch (e: Exception) {
            AuthResult.Error(e, "OAuth callback error: ${e.message}")
        }
    }

    override suspend fun getCurrentAuthState(): AuthResult<AuthState> {
        return try {
            val localState = localDataSource.getAuthState()
            if (localState?.isAuthenticated == true) {
                AuthResult.Success(localState)
            } else {
                AuthResult.Success(AuthState(isAuthenticated = false))
            }
        } catch (e: Exception) {
            AuthResult.Error(e, "Failed to get current auth state: ${e.message}")
        }
    }

    override suspend fun logout(): AuthResult<Unit> {
        return try {
            // Always clear local state, even if remote logout fails
            localDataSource.clearAuthState()

            kotlin.runCatching {
                remoteDataSource.logout()
            }

            AuthResult.Success(Unit)
        } catch (e: Exception) {
            AuthResult.Error(e, "Logout error: ${e.message}")
        }
    }

    override suspend fun refreshTokens(): AuthResult<AuthState> {
        return try {
            val currentState = localDataSource.getAuthState()
            val refreshToken = currentState?.tokens?.refreshToken
                ?: return AuthResult.Error(Exception("No refresh token"), "No refresh token available")

            val response = remoteDataSource.refreshToken(RefreshTokenDto(refreshToken))

            if (response.isSuccessful && response.body() != null) {
                val newAuthState = response.body()!!.toDomain()
                localDataSource.saveAuthState(newAuthState)
                AuthResult.Success(newAuthState)
            } else {
                // If refresh fails, clear local state
                localDataSource.clearAuthState()
                AuthResult.Error(
                    Exception("HTTP ${response.code()}"),
                    "Token refresh failed: ${response.message()}"
                )
            }
        } catch (e: Exception) {
            localDataSource.clearAuthState()
            AuthResult.Error(e, "Token refresh error: ${e.message}")
        }
    }

    override fun observeAuthState(): Flow<AuthState> {
        return localDataSource.observeAuthState()
    }

    override suspend fun isAuthenticated(): Boolean {
        return kotlin.runCatching {
            localDataSource.getAuthState()?.isAuthenticated == true
        }.getOrDefault(false)
    }
}