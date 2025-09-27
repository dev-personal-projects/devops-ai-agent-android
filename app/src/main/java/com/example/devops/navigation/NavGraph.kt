package com.example.devops.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.devops.ui.features.auth.login.presentation.view.LoginScreen
import com.example.devops.ui.features.auth.login.presentation.viewModel.LoginViewModel
import com.example.devops.ui.features.home.view.HomeScreen
import com.example.devops.ui.features.onBoarding.view.OnboardingScreen

@Composable
fun NavigationGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    navigationViewModel: NavigationViewModel = hiltViewModel(),
    loginViewModel: LoginViewModel = hiltViewModel(),
) {

    // State to track if we should show onboarding
    val showOnboarding by navigationViewModel.showOnboarding.collectAsState()
    val loginState by loginViewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    // Auto-navigate when login is successful
    LaunchedEffect(loginState.isLoggedIn) {
        if (loginState.isLoggedIn) {
            navController.navigate(Routes.Home.route) {
                popUpTo(Routes.Login.route) { inclusive = true }
                popUpTo(Routes.Onboarding.route) { inclusive = true }
            }
        }
    }

//    // Determine start destination based on onboarding status
//    val startDestination = when (showOnboarding) {
//        true -> Routes.Onboarding.route
//        false -> Routes.Login.route
//        null -> Routes.Login.route // Loading state, default to login
//    }

    // Determine start destination
    val startDestination = when {
        loginState.isLoggedIn -> Routes.Home.route
        showOnboarding == true -> Routes.Onboarding.route
        else -> Routes.Login.route
    }

    Scaffold (
        containerColor = MaterialTheme.colorScheme.background,
    ) { innerPadding ->

        // Only show navigation when we've determined the onboarding status
        if (showOnboarding == null && !loginState.isLoggedIn) {
            // Show loading while checking onboarding status
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            NavHost(
                navController = navController,
                startDestination = startDestination,
                modifier = modifier.padding(innerPadding)
            ) {

                composable(Routes.Onboarding.route) {
                    OnboardingScreen(
                        onCompleted = {
                            // Navigate to login and clear onboarding from backstack
                            navController.navigate(Routes.Login.route) {
                                popUpTo(Routes.Onboarding.route) { inclusive = true }
                            }
                        }
                    )
                }

                composable(Routes.Login.route) {
                    LoginScreen(
                        onGithubButtonClick = loginViewModel::loginWithGitHub // This now starts OAuth flow
                    )
                }

                composable(Routes.Home.route) {
                    HomeScreen()
                }
                composable(Routes.Profile.route) {
                    //ProfileScreen()

                }
            }

        }
    }

}