package com.example.devops.ui.features.auth.domain.usecase
import com.example.devops.ui.features.auth.domain.model.AuthState
import com.example.devops.ui.features.auth.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class ObserveAuthStateUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(): Flow<AuthState> {
        return authRepository.observeAuthState()
    }
}