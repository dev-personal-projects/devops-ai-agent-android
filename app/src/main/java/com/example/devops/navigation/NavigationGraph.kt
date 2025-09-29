package com.example.devops.navigation

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.devops.ui.features.auth.presentation.screen.LoginScreen
import com.example.devops.ui.features.auth.presentation.state.AuthUiState
import com.example.devops.ui.features.auth.presentation.viewmodel.AuthViewModel
import com.example.devops.ui.features.auth.presentation.event.AuthEvent
import com.example.devops.ui.features.home.presentation.screen.HomeScreen

@Composable
fun NavigationGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val authState by authViewModel.screenState.collectAsStateWithLifecycle()

    LaunchedEffect(authState.authState) {
        when (authState.authState) {
            is AuthUiState.Authenticated -> {
                if (navController.currentDestination?.route != Routes.Home.route) {
                    navController.navigate(Routes.Home.route) {
                        popUpTo(Routes.Login.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            }
            is AuthUiState.Unauthenticated -> {
                if (navController.currentDestination?.route != Routes.Login.route) {
                    navController.navigate(Routes.Login.route) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            }
            else -> { /* Handle loading and error states */ }
        }
    }

    val startDestination = Routes.Onboarding.route

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(Routes.Onboarding.route) {
            com.example.devops.ui.features.onboarding.presentation.screen.OnboardingScreen(
                onCompleted = {
                    navController.navigate(Routes.Login.route) {
                        popUpTo(Routes.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.Login.route) {
            LoginScreen(
                onNavigateToHome = {
                    navController.navigate(Routes.Home.route) {
                        popUpTo(Routes.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.Home.route) {
            HomeScreen(
                onLogout = {
                    authViewModel.handleEvent(AuthEvent.Logout)
                }
            )
        }
    }
}