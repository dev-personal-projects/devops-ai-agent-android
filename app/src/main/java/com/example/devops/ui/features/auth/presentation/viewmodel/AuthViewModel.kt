package com.example.devops.ui.features.auth.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.devops.ui.features.auth.domain.model.AuthResult
import com.example.devops.ui.features.auth.domain.repository.AuthRepository
import com.example.devops.ui.features.auth.presentation.event.AuthEvent
import com.example.devops.ui.features.auth.presentation.state.AuthUiState
import com.example.devops.ui.features.auth.presentation.state.LoginScreenState
import com.example.devops.ui.features.auth.presentation.oauth.GitHubOAuthManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import android.util.Log

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val oauthManager: GitHubOAuthManager
) : ViewModel() {

    private val _screenState = MutableStateFlow(LoginScreenState())
    val screenState: StateFlow<LoginScreenState> = _screenState.asStateFlow()

    init {
        checkAuthStatus()
        observeAuthState()
    }

    private fun observeAuthState() {
        viewModelScope.launch {
            authRepository.observeAuthState().collect { authState ->
                val uiState = if (authState.isAuthenticated && authState.user != null) {
                    AuthUiState.Authenticated(authState.user)
                } else {
                    AuthUiState.Unauthenticated
                }
                _screenState.value = _screenState.value.copy(authState = uiState)
            }
        }
    }

    fun handleEvent(event: AuthEvent) {
        when (event) {
            is AuthEvent.InitiateGitHubLogin -> initiateGitHubLogin()
            is AuthEvent.CheckAuthStatus -> checkAuthStatus()
            is AuthEvent.Logout -> logout()
            is AuthEvent.ClearError -> clearError()
            is AuthEvent.DismissWelcomeMessage -> dismissWelcomeMessage()
            is AuthEvent.HandleOAuthCallback -> handleOAuthCallback(event.code, event.state)
            is AuthEvent.HandleDirectTokens -> handleDirectTokens(event.accessToken, event.refreshToken, event.userId)
        }
    }

    private fun initiateGitHubLogin() {
        viewModelScope.launch {
            _screenState.value = _screenState.value.copy(isOAuthLoading = true, errorMessage = null)
            
            try {
                oauthManager.startOAuthFlow()
                _screenState.value = _screenState.value.copy(isOAuthLoading = false)
            } catch (e: Exception) {
                _screenState.value = _screenState.value.copy(
                    isOAuthLoading = false,
                    errorMessage = "Failed to start GitHub login: ${e.message}"
                )
            }
        }
    }

    private fun checkAuthStatus() {
        viewModelScope.launch {
            when (val result = authRepository.getCurrentAuthState()) {
                is AuthResult.Success -> {
                    val uiState = if (result.data.isAuthenticated && result.data.user != null) {
                        AuthUiState.Authenticated(result.data.user)
                    } else {
                        AuthUiState.Unauthenticated
                    }
                    _screenState.value = _screenState.value.copy(authState = uiState)
                }
                is AuthResult.Error -> {
                    _screenState.value = _screenState.value.copy(
                        authState = AuthUiState.Unauthenticated,
                        errorMessage = result.message
                    )
                }
            }
        }
    }

    private fun logout() {
        viewModelScope.launch {
            authRepository.logout()
            _screenState.value = _screenState.value.copy(authState = AuthUiState.Unauthenticated)
        }
    }

    private fun clearError() {
        _screenState.value = _screenState.value.copy(errorMessage = null)
    }

    private fun dismissWelcomeMessage() {
        _screenState.value = _screenState.value.copy(showWelcomeMessage = false)
    }

    private fun handleOAuthCallback(code: String, state: String) {
        viewModelScope.launch {
            Log.d("AuthViewModel", "Handling OAuth callback with code: $code, state: $state")
            _screenState.value = _screenState.value.copy(isOAuthLoading = true)
            
            when (val result = authRepository.handleOAuthCallback(code, state)) {
                is AuthResult.Success -> {
                    Log.d("AuthViewModel", "OAuth callback successful")
                    val uiState = if (result.data.user != null) {
                        AuthUiState.Authenticated(result.data.user)
                    } else {
                        AuthUiState.Unauthenticated
                    }
                    _screenState.value = _screenState.value.copy(
                        authState = uiState,
                        isOAuthLoading = false
                    )
                }
                is AuthResult.Error -> {
                    Log.e("AuthViewModel", "OAuth callback failed: ${result.message}")
                    _screenState.value = _screenState.value.copy(
                        authState = AuthUiState.Unauthenticated,
                        isOAuthLoading = false,
                        errorMessage = result.message
                    )
                }
            }
        }
    }

    private fun handleDirectTokens(accessToken: String, refreshToken: String?, userId: String?) {
        viewModelScope.launch {
            Log.d("AuthViewModel", "Handling direct tokens from backend redirect")
            _screenState.value = _screenState.value.copy(isOAuthLoading = true)
            
            when (val result = authRepository.storeDirectTokens(accessToken, refreshToken, userId)) {
                is AuthResult.Success -> {
                    Log.d("AuthViewModel", "Direct tokens stored successfully")
                    val uiState = if (result.data.user != null) {
                        AuthUiState.Authenticated(result.data.user)
                    } else {
                        AuthUiState.Unauthenticated
                    }
                    _screenState.value = _screenState.value.copy(
                        authState = uiState,
                        isOAuthLoading = false
                    )
                }
                is AuthResult.Error -> {
                    Log.e("AuthViewModel", "Failed to store direct tokens: ${result.message}")
                    _screenState.value = _screenState.value.copy(
                        authState = AuthUiState.Unauthenticated,
                        isOAuthLoading = false,
                        errorMessage = result.message
                    )
                }
            }
        }
    }
}