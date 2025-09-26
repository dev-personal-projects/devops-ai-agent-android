package com.example.devops.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.devops.ui.features.auth.login.view.LoginScreen
import com.example.devops.ui.features.home.view.HomeScreen

@Composable
fun NavigationGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {

    val startDestination = Routes.Login.route

    Scaffold (
        containerColor = MaterialTheme.colorScheme.background,
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = modifier.padding(innerPadding)
        ) {
            composable(Routes.Login.route) {
                LoginScreen(
                    onGithubButtonClick = {
                        navController.navigate(Routes.Home.route)
                    }
                )
            }
            composable(Routes.Onboarding.route) {
                //OnboardingScreen()
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