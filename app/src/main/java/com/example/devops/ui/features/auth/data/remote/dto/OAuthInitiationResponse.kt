package com.example.devops.ui.features.auth.data.remote.dto

data class OAuthInitiationResponse(
    val auth_url: String,
    val state: String,
    val provider: String
)