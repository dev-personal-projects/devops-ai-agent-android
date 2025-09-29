package com.example.devops.ui.features.auth.presentation.screen

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.devops.ui.features.auth.presentation.components.*
import com.example.devops.ui.features.auth.presentation.event.AuthEvent
import com.example.devops.ui.features.auth.presentation.state.AuthUiState
import com.example.devops.ui.features.auth.presentation.viewmodel.AuthViewModel

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel = hiltViewModel(),
    onNavigateToHome: () -> Unit = {}
) {
    val screenState by authViewModel.screenState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(screenState.authState) {
        if (screenState.authState is AuthUiState.Authenticated) {
            onNavigateToHome()
        }
    }

    LaunchedEffect(Unit) {
        val intent = (context as? android.app.Activity)?.intent
        val oauthError = intent?.getStringExtra("oauth_error")
        if (oauthError != null) {
            authViewModel.handleEvent(AuthEvent.ClearError)
        }
    }

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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            
            Spacer(modifier = Modifier.height(60.dp))

            AnimatedVisibility(
                visible = screenState.showWelcomeMessage,
                enter = slideInVertically() + fadeIn(),
                exit = slideOutVertically() + fadeOut()
            ) {
                WelcomeCard(
                    onDismiss = { 
                        authViewModel.handleEvent(AuthEvent.DismissWelcomeMessage) 
                    }
                )
            }

            LogoSection()

            Spacer(modifier = Modifier.height(32.dp))

            AuthenticationSection(
                isLoading = screenState.isLoading,
                onGitHubLogin = { 
                    authViewModel.handleEvent(AuthEvent.InitiateGitHubLogin) 
                }
            )

            AnimatedVisibility(
                visible = screenState.errorMessage != null,
                enter = slideInVertically() + fadeIn(),
                exit = slideOutVertically() + fadeOut()
            ) {
                screenState.errorMessage?.let { message ->
                    ErrorCard(
                        message = message,
                        onDismiss = { 
                            authViewModel.handleEvent(AuthEvent.ClearError) 
                        },
                        onRetry = { 
                            authViewModel.handleEvent(AuthEvent.CheckAuthStatus) 
                        }
                    )
                }
            }

            AnimatedVisibility(
                visible = screenState.isLoading,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                LoadingIndicator()
            }

            Spacer(modifier = Modifier.height(32.dp))

            SecurityFooter()

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}