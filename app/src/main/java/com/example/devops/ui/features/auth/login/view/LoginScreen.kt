package com.example.devops.ui.features.auth.login.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.devops.ui.features.auth.login.components.ErrorMessage
import com.example.devops.ui.features.auth.login.components.FooterSection
import com.example.devops.ui.features.auth.login.components.GitHubLoginButton
import com.example.devops.ui.features.auth.login.components.LogoSection
import com.example.devops.ui.features.auth.login.components.WelcomeSection


@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onLoginSuccess: () -> Unit = {},
    onGithubButtonClick: () -> Unit = {},
) {
    val uiState = true

    // Animation states
    val infiniteTransition = rememberInfiniteTransition(label = "background")
    val backgroundRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(20000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    // Handle login success
//    LaunchedEffect(uiState) {
//        if (uiState) {
//            onLoginSuccess()
//        }
//    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        MaterialTheme.colorScheme.background
                    ),
                    radius = 1000f
                )
            )
    ) {

        // Main content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Welcome animation
            AnimatedVisibility(
                visible = uiState,
                enter = slideInVertically(
                    initialOffsetY = { -100 },
                    animationSpec = tween(800, easing = FastOutSlowInEasing)
                ) + fadeIn(animationSpec = tween(800)),
                exit = slideOutVertically(
                    targetOffsetY = { -100 },
                    animationSpec = tween(500)
                ) + fadeOut(animationSpec = tween(500))
            ) {
                WelcomeSection(onDismiss = {})
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Logo and title
            LogoSection()

            Spacer(modifier = Modifier.height(48.dp))

            // GitHub login button
            GitHubLoginButton(
                isLoading = false,
                onClick = onGithubButtonClick
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Error message
            AnimatedVisibility(
                visible = true,
                enter = slideInVertically() + fadeIn(),
                exit = slideOutVertically() + fadeOut()
            ) {
                ErrorMessage(
                    message = "Error message",
                    onDismiss = {}
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Footer
            FooterSection()
        }
    }
}