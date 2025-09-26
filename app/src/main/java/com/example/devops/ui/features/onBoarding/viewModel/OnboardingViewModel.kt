package com.example.devops.ui.features.onBoarding.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.devops.sharedPrefs.onBoardPref.OnboardingPreferencesManager
import com.example.devops.ui.features.onBoarding.data.state.OnboardingUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val preferencesManager: OnboardingPreferencesManager
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()

    fun nextPage() {
        val currentPage = _uiState.value.currentPage
        if (currentPage < 2) { // 3 slides (0, 1, 2)
            _uiState.value = _uiState.value.copy(
                currentPage = currentPage + 1,
                showSkipButton = currentPage + 1 < 2
            )
        } else {
            completeOnboarding()
        }
    }

    fun previousPage() {
        val currentPage = _uiState.value.currentPage
        if (currentPage > 0) {
            _uiState.value = _uiState.value.copy(
                currentPage = currentPage - 1,
                showSkipButton = true
            )
        }
    }

    fun goToPage(page: Int) {
        _uiState.value = _uiState.value.copy(
            currentPage = page,
            showSkipButton = page < 2
        )
    }

    fun completeOnboarding() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isCompleted = true)
            preferencesManager.setOnboardingCompleted()
        }
    }
}