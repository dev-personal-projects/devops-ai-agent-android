package com.example.devops.navigation

sealed class Routes(val route: String) {
    object Onboarding : Routes("onboarding")
    object Login : Routes("login")
    object Home : Routes("home")
    object Profile : Routes("profile")
}