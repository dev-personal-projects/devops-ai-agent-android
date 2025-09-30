package com.example.devops.ui.features.auth.presentation.event

sealed class AuthEvent {
    object InitiateGitHubLogin : AuthEvent()
    object CheckAuthStatus : AuthEvent()
    object Logout : AuthEvent()
    object ClearError : AuthEvent()
    object DismissWelcomeMessage : AuthEvent()
    data class HandleOAuthCallback(val code: String, val state: String) : AuthEvent()
    data class HandleDirectTokens(val accessToken: String, val refreshToken: String?, val userId: String?) : AuthEvent()
}