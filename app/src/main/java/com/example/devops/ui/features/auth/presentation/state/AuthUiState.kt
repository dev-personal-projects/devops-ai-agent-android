package com.example.devops.ui.features.auth.presentation.state

import com.example.devops.ui.features.auth.domain.model.User

sealed class AuthUiState {
    object Initial : AuthUiState()
    object Loading : AuthUiState()
    object Unauthenticated : AuthUiState()
    data class Authenticated(val user: User) : AuthUiState()
    data class Error(val message: String) : AuthUiState()
}