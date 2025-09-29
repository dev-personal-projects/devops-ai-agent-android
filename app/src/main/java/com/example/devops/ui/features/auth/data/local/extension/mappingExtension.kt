package com.example.devops.ui.features.auth.data.local.extension
import com.example.devops.ui.features.auth.data.local.model.AuthTokensStorageModel
import com.example.devops.ui.features.auth.data.local.model.UserStorageModel

import com.example.devops.ui.features.auth.domain.model.AuthTokens
import com.example.devops.ui.features.auth.domain.model.User
fun User.toStorageModel() = UserStorageModel(id, login, name, email, avatarUrl, company, location, bio)
fun UserStorageModel.toDomain() = User(id, login, name, email, avatarUrl, company, location, bio)
fun AuthTokens.toStorageModel() = AuthTokensStorageModel(accessToken, refreshToken, expiresIn, tokenType)
fun AuthTokensStorageModel.toDomain() = AuthTokens(accessToken, refreshToken, expiresIn, tokenType)

class AuthStorageException(message: String, cause: Throwable? = null) : Exception(message, cause)