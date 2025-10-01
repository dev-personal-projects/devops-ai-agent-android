package com.example.devops.ui.features.auth.data.remote.api

import com.example.devops.ui.features.auth.data.remote.dto.*
import retrofit2.Response
import retrofit2.http.*

interface GitHubAuthApi {
    
    @GET("auth/oauth/github")
    suspend fun initiateOAuth(@Query("mobile") mobile: Boolean = true): Response<OAuthInitiationResponse>
    
    @POST("auth/oauth/github/callback/api")
    suspend fun handleCallback(
        @Body request: OAuthCallbackDto
    ): Response<AuthResponseDto>
    
    @POST("auth/refresh")
    suspend fun refreshToken(@Body request: RefreshTokenDto): Response<AuthResponseDto>
    
    @POST("auth/logout")
    suspend fun logout(): Response<Unit>
}