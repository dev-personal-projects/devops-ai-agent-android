package com.example.devops.ui.features.home.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "🏠",
            style = MaterialTheme.typography.displayLarge
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Welcome to Home!",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "You've successfully completed the onboarding flow!",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}