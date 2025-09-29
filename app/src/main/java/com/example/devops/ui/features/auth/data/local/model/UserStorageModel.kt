package com.example.devops.ui.features.auth.data.local.model

import com.example.devops.ui.features.auth.domain.model.User

data class UserStorageModel(
    val id: String,
    val login: String,
    val name: String?,
    val email: String?,
    val avatarUrl: String,
    val company: String?,
    val location: String?,
    val bio: String?
) {
    fun toDomain(): User = User(
        id = id,
        login = login,
        name = name,
        email = email,
        avatarUrl = avatarUrl,
        company = company,
        location = location,
        bio = bio
    )
}

fun User.toStorageModel(): UserStorageModel = UserStorageModel(
    id = id,
    login = login,
    name = name,
    email = email,
    avatarUrl = avatarUrl,
    company = company,
    location = location,
    bio = bio
)