package com.example.devops.ui.features.auth.login.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.devops.di.GitHubOAuthManager
import com.example.devops.ui.features.auth.login.data.model.GitHubUser
import com.example.devops.ui.features.auth.login.data.repo.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginUiState(
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val error: String? = null,
    val user: GitHubUser? = null,
    val showWelcome: Boolean = true
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val oauthManager: GitHubOAuthManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    init {
        checkExistingLogin()
    }

    private fun checkExistingLogin() {
        if (authRepository.isLoggedIn()) {
            val user = authRepository.getStoredUser()
            _uiState.value = _uiState.value.copy(
                isLoggedIn = true,
                user = user
            )
        }
    }

    /**
     * Starts the GitHub OAuth browser flow by fetching the authorization URL.
     */
    fun loginWithGitHub() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            try {
                // Delegating the browser opening logic to the platform-specific manager
                oauthManager.startOAuthFlow()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Failed to start OAuth: ${e.message}"
                )
            }
        }
    }

    /**
     * Called by OAuthCallbackActivity after the browser redirect.
     */
    fun handleOAuthCallback(code: String, state: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            authRepository.handleGitHubCallback(code, state).fold(
                onSuccess = { authResponse ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isLoggedIn = true,
                        user = authResponse.user
                    )
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "Login failed: ${error.message}"
                    )
                }
            )
        }
    }

    fun setOAuthError(error: String) {
        _uiState.value = _uiState.value.copy(
            isLoading = false,
            error = error
        )
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout().fold(
                onSuccess = {
                    _uiState.value = LoginUiState() // Reset to initial state
                },
                onFailure = { error ->
                    // Since local tokens are always cleared in repository, we reset the state,
                    // but log/show a warning about the backend failure.
                    _uiState.value = LoginUiState(
                        error = "Logout warning: Failed to notify backend. ${error.message}"
                    )
                }
            )
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}
