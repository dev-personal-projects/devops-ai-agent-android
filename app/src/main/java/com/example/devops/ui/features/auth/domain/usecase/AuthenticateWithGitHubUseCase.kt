package com.example.devops.ui.features.auth.domain.usecase
import com.example.devops.ui.features.auth.domain.model.AuthResult
import com.example.devops.ui.features.auth.domain.repository.AuthRepository
import javax.inject.Inject

class AuthenticateWithGitHubUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): AuthResult<String> {
        return try {
            authRepository.initiateGitHubOAuth()
        } catch (e: Exception) {
            AuthResult.Error("Failed to initiate GitHub OAuth: ${e.message}", e)
        }
    }
}