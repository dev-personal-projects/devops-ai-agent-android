package com.example.devops.ui.features.onBoarding.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.example.devops.ui.features.onBoarding.data.model.onboardingPages
import java.lang.Math.toRadians

@Composable
fun FloatingShape(
    rotation: Float,
    index: Int,
    currentPage: Int
) {
    val density = LocalDensity.current
    val screenWidth = with(density) { 400.dp.toPx() }
    val screenHeight = with(density) { 800.dp.toPx() }

    val animatedAlpha by animateFloatAsState(
        targetValue = if (currentPage == index % 3) 0.3f else 0.1f,
        animationSpec = tween(1000),
        label = "shape_alpha"
    )

    val offsetX = screenWidth * 0.2f + (kotlin.math.cos(toRadians((rotation + index * 60).toDouble())) * 100).toFloat()
    val offsetY = screenHeight * 0.3f + (kotlin.math.sin(toRadians((rotation + index * 60).toDouble())) * 150).toFloat()

    Box(
        modifier = Modifier
            .offset(
                x = with(density) { offsetX.toDp() },
                y = with(density) { offsetY.toDp() }
            )
            .size((40 + index * 10).dp)
            .alpha(animatedAlpha)
            .background(
                color = Color(onboardingPages[currentPage].accentColor),
                shape = if (index % 2 == 0) CircleShape else RoundedCornerShape(12.dp)
            )
    )
}