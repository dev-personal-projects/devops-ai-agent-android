package com.example.devops.ui.features.auth.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.devops.ui.features.auth.presentation.components.GitHubLoginButton
import com.example.devops.ui.features.auth.presentation.event.AuthEvent
import com.example.devops.ui.features.auth.presentation.state.AuthUiState
import com.example.devops.ui.features.auth.presentation.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel = hiltViewModel(),
    onNavigateToHome: () -> Unit = {}
) {
    val screenState by authViewModel.screenState.collectAsStateWithLifecycle()

    LaunchedEffect(screenState.authState) {
        if (screenState.authState is AuthUiState.Authenticated) {
            onNavigateToHome()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "🚀",
            style = MaterialTheme.typography.displayLarge,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Text(
            text = "Welcome to DevOps Hub",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = "Connect your GitHub account to get started",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            modifier = Modifier.padding(bottom = 32.dp)
        )

        GitHubLoginButton(
            isLoading = screenState.isLoading,
            onClick = {
                authViewModel.handleEvent(AuthEvent.InitiateGitHubLogin)
            },
            modifier = Modifier.fillMaxWidth()
        )

        screenState.errorMessage?.let { error ->
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Text(
                    text = error,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}