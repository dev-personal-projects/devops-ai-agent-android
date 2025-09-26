package com.example.devops.navigation

sealed class Routes(
    val route: String,
    val hasBottomNav: Boolean = false,
) {
    object Login : Routes("Login", hasBottomNav = false)
    object Onboarding : Routes("Onboarding", hasBottomNav = false)
    object Home : Routes("Home", hasBottomNav = true)
    object Profile : Routes("Profile", hasBottomNav = true)

}