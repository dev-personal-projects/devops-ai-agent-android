package com.example.devops.ui.features.auth.login.data.model

data class OAuthUrlResponse(
    val authorization_url: String,
    val state: String
)