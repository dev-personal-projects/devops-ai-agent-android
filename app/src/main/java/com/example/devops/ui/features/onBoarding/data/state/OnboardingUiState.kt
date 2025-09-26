package com.example.devops.ui.features.onBoarding.data.state

data class OnboardingUiState(
    val currentPage: Int = 0,
    val isCompleted: Boolean = false,
    val showSkipButton: Boolean = true
)