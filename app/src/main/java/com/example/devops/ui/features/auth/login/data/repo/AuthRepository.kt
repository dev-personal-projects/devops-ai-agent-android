package com.example.devops.ui.features.auth.login.data.repo

import com.example.devops.ui.features.auth.login.data.api.AuthApi
import com.example.devops.ui.features.auth.login.data.model.AuthResponse
import com.example.devops.ui.features.auth.login.data.model.GitHubCallbackRequest
import com.example.devops.ui.features.auth.login.data.model.GitHubUser
import com.example.devops.ui.features.auth.login.data.model.OAuthUrlResponse
import com.example.devops.ui.features.auth.login.data.storage.TokenManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Concrete implementation of the IAuthRepository contract.
 * Handles API calls, error wrapping (using Kotlin's Result), and token management.
 */
@Singleton
class AuthRepository @Inject constructor(
    private val authApi: AuthApi,
    private val tokenManager: TokenManager,
) {

    suspend fun getGitHubAuthUrl(): Result<OAuthUrlResponse> = withContext(Dispatchers.IO) {
        try {
            val response = authApi.getGitHubAuthUrl()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to get auth URL: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend  fun handleGitHubCallback(code: String, state: String): Result<AuthResponse> = withContext(Dispatchers.IO) {
        try {
            val request = GitHubCallbackRequest(code, state)
            val response = authApi.handleGitHubCallback(request)

            if (response.isSuccessful && response.body() != null) {
                val authResponse = response.body()!!

                // Save tokens securely
                tokenManager.saveAccessToken(authResponse.access_token)
                tokenManager.saveUserInfo(
                    com.google.gson.Gson().toJson(authResponse.user)
                )

                Result.success(authResponse)
            } else {
                Result.failure(Exception("Authentication failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getCurrentUser(): Result<GitHubUser> = withContext(Dispatchers.IO) {
        try {
            val response = authApi.getCurrentUser()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to get user info: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend  fun logout(): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val response = authApi.logout()
            tokenManager.clearTokens()

            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                // Even if backend logout fails, clear local tokens
                Result.success(Unit)
            }
        } catch (e: Exception) {
            // Clear local tokens even on network error
            tokenManager.clearTokens()
            Result.success(Unit)
        }
    }

    fun isLoggedIn(): Boolean = tokenManager.isLoggedIn()

    fun getStoredUser(): GitHubUser? {
        return try {
            val userJson = tokenManager.getUserInfo()
            if (userJson != null) {
                com.google.gson.Gson().fromJson(userJson, GitHubUser::class.java)
            } else null
        } catch (e: Exception) {
            null
        }
    }
}