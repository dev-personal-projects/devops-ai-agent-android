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
            android.util.Log.d("AuthRepository", "Handling OAuth callback - code: ${code.take(10)}..., state: ${state.take(10)}...")
            val request = OAuthCallbackDto(code = code, state = state)
            val response = remoteDataSource.handleCallback(request, mobile = true)
            
            android.util.Log.d("AuthRepository", "Response code: ${response.code()}, isSuccessful: ${response.isSuccessful}")
            
            if (response.isSuccessful && response.body() != null) {
                val authState = response.body()!!.toDomain()
                _authState.value = authState
                android.util.Log.d("AuthRepository", "OAuth callback successful, user: ${authState.user?.login}")
                AuthResult.Success(authState)
            } else {
                val errorBody = response.errorBody()?.string()
                android.util.Log.e("AuthRepository", "OAuth callback failed: ${response.code()} ${response.message()}. Error: $errorBody")
                AuthResult.Error("OAuth callback failed: ${response.code()} ${response.message()}. Error: $errorBody")
            }
        } catch (e: Exception) {
            android.util.Log.e("AuthRepository", "OAuth callback exception: ${e.message}", e)
            AuthResult.Error("OAuth callback error: ${e.message}", e)
        }
    }

    override suspend fun storeDirectTokens(accessToken: String, refreshToken: String?, userId: String?): AuthResult<AuthState> {
        return try {
            android.util.Log.d("AuthRepository", "Storing direct tokens - userId: $userId")
            
            // Create a basic user object (we'll need to fetch full user info later)
            val user = userId?.let {
                com.example.devops.ui.features.auth.domain.model.User(
                    id = it,
                    login = "user", // Placeholder
                    name = "User", // Placeholder
                    email = null,
                    avatarUrl = "",
                    company = null,
                    location = null,
                    bio = null
                )
            }
            
            val tokens = com.example.devops.ui.features.auth.domain.model.AuthTokens(
                accessToken = accessToken,
                refreshToken = refreshToken,
                expiresIn = 3600L, // 1 hour default
                tokenType = "Bearer"
            )
            
            val authState = com.example.devops.ui.features.auth.domain.model.AuthState(
                isAuthenticated = true,
                user = user,
                tokens = tokens
            )
            
            _authState.value = authState
            android.util.Log.d("AuthRepository", "Direct tokens stored successfully")
            AuthResult.Success(authState)
            
        } catch (e: Exception) {
            android.util.Log.e("AuthRepository", "Failed to store direct tokens: ${e.message}", e)
            AuthResult.Error("Failed to store tokens: ${e.message}", e)
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