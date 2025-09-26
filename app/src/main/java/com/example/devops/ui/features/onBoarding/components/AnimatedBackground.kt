package com.example.devops.ui.features.onBoarding.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import com.example.devops.ui.features.onBoarding.data.model.onboardingPages
import androidx.compose.ui.graphics.Color
import androidx.tv.material3.MaterialTheme

@Composable
fun AnimatedBackground(currentPage: Int) {
    val infiniteTransition = rememberInfiniteTransition(label = "background")
    
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(30000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    val backgroundColor by animateColorAsState(
        targetValue = Color(onboardingPages[currentPage].backgroundColor).copy(alpha = 0.1f),
        animationSpec = tween(800),
        label = "background_color"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        backgroundColor,
                        MaterialTheme.colorScheme.background
                    ),
                    radius = 1200f
                )
            )
    ) {
        // Floating shapes
        repeat(6) { index ->
            FloatingShape(
                rotation = rotation,
                index = index,
                currentPage = currentPage
            )
        }
    }
}