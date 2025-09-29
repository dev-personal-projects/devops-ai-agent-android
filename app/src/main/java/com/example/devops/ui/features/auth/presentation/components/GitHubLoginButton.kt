package com.example.devops.ui.features.auth.presentation.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp

@Composable
fun GitHubLoginButton(
    isLoading: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val buttonScale by animateFloatAsState(
        targetValue = if (isLoading) 0.95f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "button_scale"
    )

    Button(
        onClick = onClick,
        enabled = !isLoading,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .graphicsLayer {
                scaleX = buttonScale
                scaleY = buttonScale
            },
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        shape = MaterialTheme.shapes.large,
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 4.dp,
            pressedElevation = 1.dp,
            disabledElevation = 0.dp
        )
    ) {
        AnimatedContent(
            targetState = isLoading,
            transitionSpec = {
                fadeIn(animationSpec = tween(200)) togetherWith
                        fadeOut(animationSpec = tween(200))
            },
            label = "button_content"
        ) { loading ->
            if (loading) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Connecting to GitHub...",
                        style = MaterialTheme.typography.titleSmall
                    )
                }
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "🐙",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        text = "Continue with GitHub",
                        style = MaterialTheme.typography.titleSmall
                    )
                }
            }
        }
    }
}