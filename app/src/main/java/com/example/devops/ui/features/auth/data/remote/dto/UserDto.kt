package com.example.devops.ui.features.auth.data.remote.dto

import com.example.devops.ui.features.auth.domain.model.User
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    @SerialName("id") val id: String,
    @SerialName("login") val login: String,
    @SerialName("fullName") val name: String?,
    @SerialName("email") val email: String?,
    @SerialName("avatar_url") val avatarUrl: String,
    @SerialName("company") val company: String? = null,
    @SerialName("location") val location: String? = null,
    @SerialName("bio") val bio: String? = null
)

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