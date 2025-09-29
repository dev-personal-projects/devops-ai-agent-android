package com.example.devops.ui.features.auth.data.mapper

import com.example.devops.ui.features.auth.data.remote.dto.UserDto

import com.example.devops.ui.features.auth.domain.model.User

fun UserDto.toDomain(): User {
    return User(
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

fun User.toDto(): UserDto {
    return UserDto(
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