package com.example.devops.ui.features.auth.domain.usecase
import com.example.devops.ui.features.auth.domain.model.AuthResult
import com.example.devops.ui.features.auth.domain.model.AuthState
import com.example.devops.ui.features.auth.domain.repository.AuthRepository
import javax.inject.Inject

class HandleOAuthCallbackUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(code: String, state: String): AuthResult<AuthState> {
        return try {
            authRepository.handleOAuthCallback(code, state)
        } catch (e: Exception) {
            AuthResult.Error("OAuth callback failed: ${e.message}", e)
        }
    }
}