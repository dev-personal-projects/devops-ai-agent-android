package com.example.devops.ui.features.auth.login.data.model

data class GitHubCallbackRequest(
    val code: String,
    val state: String
)