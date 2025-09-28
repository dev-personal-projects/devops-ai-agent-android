package com.example.devops.ui.features.auth.login.domain.repository

import com.example.devops.ui.features.auth.login.data.model.AuthResponse
import com.example.devops.ui.features.auth.login.data.model.GitHubUser
import com.example.devops.ui.features.auth.login.data.model.OAuthUrlResponse

interface IAuthReposittttory {
    suspend fun getGitHubAuthUrl(): Result<OAuthUrlResponse>
    suspend fun handleGitHubCallback(code: String, state: String): Result<AuthResponse>
    suspend fun getCurrentUser(): Result<GitHubUser>
    suspend fun logout(): Result<Unit>
    fun isLoggedIn(): Boolean
    fun getStoredUser(): GitHubUser?
}