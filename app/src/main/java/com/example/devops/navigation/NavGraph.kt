package com.example.devops.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
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
import com.example.devops.ui.features.home.view.HomeScreen
import com.example.devops.ui.features.onBoarding.view.OnboardingScreen

@Composable
fun NavigationGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    navigationViewModel: NavigationViewModel = hiltViewModel()
) {

    // State to track if we should show onboarding
    val showOnboarding by navigationViewModel.showOnboarding.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    // Check onboarding status on first composition
//    LaunchedEffect(Unit) {
//        coroutineScope.launch {
//            showOnboarding = !preferencesManager.hasSeenOnboarding()
//        }
//    }

    // Determine start destination based on onboarding status
    val startDestination = when (showOnboarding) {
        true -> Routes.Onboarding.route
        false -> Routes.Login.route
        null -> Routes.Login.route // Loading state, default to login
    }

    Scaffold (
        containerColor = MaterialTheme.colorScheme.background,
    ) { innerPadding ->

        // Only show navigation when we've determined the onboarding status
        if (showOnboarding == null) {
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
                        onGithubButtonClick = {
                            navController.navigate(Routes.Home.route) {
                                // Clear login from backstack after successful login
                                popUpTo(Routes.Login.route) { inclusive = true }
                            }
                        }
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