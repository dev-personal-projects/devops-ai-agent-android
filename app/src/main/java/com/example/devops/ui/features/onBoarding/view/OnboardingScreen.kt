package com.example.devops.ui.features.onBoarding.view

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.devops.ui.features.onBoarding.components.NavigationButtons
import com.example.devops.ui.features.onBoarding.components.OnboardingPageContent
import com.example.devops.ui.features.onBoarding.components.PageIndicators
import com.example.devops.ui.features.onBoarding.components.TopBar
import com.example.devops.ui.features.onBoarding.data.model.onboardingPages
import com.example.devops.ui.features.onBoarding.viewModel.OnboardingViewModel
import kotlinx.coroutines.launch

@Composable
fun OnboardingScreen(
    onboardingViewModel: OnboardingViewModel = hiltViewModel(),
    onCompleted: () -> Unit = {}
) {
    val uiState by onboardingViewModel.uiState.collectAsState()
    val pagerState = rememberPagerState(pageCount = { onboardingPages.size })
    val coroutineScope = rememberCoroutineScope()

    // Sync pager with viewmodel
    LaunchedEffect(uiState.currentPage) {
        pagerState.animateScrollToPage(uiState.currentPage)
    }

    LaunchedEffect(pagerState.currentPage) {
        if (pagerState.currentPage != uiState.currentPage) {
            onboardingViewModel.goToPage(pagerState.currentPage)
        }
    }

    // Handle completion
    LaunchedEffect(uiState.isCompleted) {
        if (uiState.isCompleted) {
            onCompleted()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Animated background
        //AnimatedBackground(currentPage = uiState.currentPage)

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Skip button
            TopBar(
                showSkip = uiState.showSkipButton,
                onSkip = onboardingViewModel::completeOnboarding
            )

            // Pager content
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f)
            ) { page ->
                OnboardingPageContent(
                    page = onboardingPages[page],
                    isActive = page == uiState.currentPage
                )
            }

            // Page indicators
            PageIndicators(
                pageCount = onboardingPages.size,
                currentPage = uiState.currentPage,
                onClick = { page ->
                    coroutineScope.launch {
                        onboardingViewModel.goToPage(page)
                    }
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Navigation buttons
            NavigationButtons(
                currentPage = uiState.currentPage,
                totalPages = onboardingPages.size,
                onNext = onboardingViewModel::nextPage,
                onPrevious = onboardingViewModel::previousPage,
                onComplete = onboardingViewModel::completeOnboarding
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}