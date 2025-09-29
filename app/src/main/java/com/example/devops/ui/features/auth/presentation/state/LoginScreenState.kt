package com.example.devops.ui.features.auth.presentation.state


data class LoginScreenState(
    val authState: AuthUiState = AuthUiState.Initial,
    val isOAuthLoading: Boolean = false,
    val showWelcomeMessage: Boolean = true,
    val errorMessage: String? = null
) {
    val isLoading: Boolean get() = authState is AuthUiState.Loading || isOAuthLoading
    val isAuthenticated: Boolean get() = authState is AuthUiState.Authenticated
}