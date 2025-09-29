package com.example.devops.ui.features.auth.domain.model

sealed class AuthResult<out T> {
    data class Success<T>(val data: T) : AuthResult<T>()
    data class Error(val message: String, val exception: Throwable? = null) : AuthResult<Nothing>()
}