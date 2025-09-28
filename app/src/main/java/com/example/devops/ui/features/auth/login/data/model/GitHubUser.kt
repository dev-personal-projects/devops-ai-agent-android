package com.example.devops.ui.features.auth.login.data.model

data class GitHubUser(
    val id: Long,
    val login: String,
    val name: String?,
    val email: String?,
    val avatar_url: String,
    val company: String?,
    val location: String?,
    val bio: String?
)