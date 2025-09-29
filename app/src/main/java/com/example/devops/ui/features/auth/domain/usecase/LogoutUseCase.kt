package com.example.devops.ui.features.auth.domain.usecase
import com.example.devops.ui.features.auth.domain.model.AuthResult
import com.example.devops.ui.features.auth.domain.repository.AuthRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): AuthResult<Unit> {
        return try {
            authRepository.logout()
            AuthResult.Success(Unit)
        } catch (e: Exception) {
            AuthResult.Error(e, "Logout failed: ${e.message}")
        }
    }
}