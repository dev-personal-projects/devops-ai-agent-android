package com.example.devops.ui.features.auth.data.remote.dto

data class OAuthCallbackDto(
    val code: String,
    val state: String
)