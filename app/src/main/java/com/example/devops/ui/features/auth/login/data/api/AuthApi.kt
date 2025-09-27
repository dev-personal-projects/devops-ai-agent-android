package com.example.devops.ui.features.auth.login.data.api

import com.example.devops.ui.features.auth.login.data.model.AuthResponse
import com.example.devops.ui.features.auth.login.data.model.GitHubCallbackRequest
import com.example.devops.ui.features.auth.login.data.model.GitHubUser
import com.example.devops.ui.features.auth.login.data.model.OAuthUrlResponse
import retrofit2.Response
import retrofit2.http.*

interface AuthApi {
    
    @GET("auth/oauth/github")
    suspend fun getGitHubAuthUrl(): Response<OAuthUrlResponse>
    
    @POST("auth/oauth/github/callback")
    suspend fun handleGitHubCallback(
        @Body request: GitHubCallbackRequest
    ): Response<AuthResponse>
    
    @POST("auth/logout")
    suspend fun logout(): Response<Unit>
    
    @GET("auth/me")
    suspend fun getCurrentUser(): Response<GitHubUser>
}