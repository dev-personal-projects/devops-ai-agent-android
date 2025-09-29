package com.example.devops.ui.features.auth.domain.model

data class User(
    val id: String,
    val login: String,
    val name: String?,
    val email: String?,
    val avatarUrl: String,
    val company: String?,
    val location: String?,
    val bio: String?
)
