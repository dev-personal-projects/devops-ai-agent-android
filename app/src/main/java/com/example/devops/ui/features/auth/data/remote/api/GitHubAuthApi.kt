package com.example.devops.ui.features.auth.data.remote.api

import com.example.devops.ui.features.auth.data.remote.dto.*
import retrofit2.Response
import retrofit2.http.*

interface GitHubAuthApi {
    
    @GET("oauth/github/mobile")
    suspend fun initiateOAuth(): Response<OAuthInitiationResponse>
    
    @POST("oauth/github/callback")
    suspend fun handleCallback(@Body request: OAuthCallbackDto): Response<AuthResponseDto>
    
    @POST("refresh")
    suspend fun refreshToken(@Body request: RefreshTokenDto): Response<AuthResponseDto>
    
    @POST("logout")
    suspend fun logout(): Response<Unit>
}