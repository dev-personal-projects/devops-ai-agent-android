package com.example.devops.ui.features.onBoarding.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NavigationButtons(
    currentPage: Int,
    totalPages: Int,
    onNext: () -> Unit,
    onPrevious: () -> Unit,
    onComplete: () -> Unit
) {
    val isLastPage = currentPage == totalPages - 1

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Previous button
        AnimatedVisibility(
            visible = currentPage > 0,
            enter = fadeIn() + slideInHorizontally(initialOffsetX = { -100 }),
            exit = fadeOut() + slideOutHorizontally(targetOffsetX = { -100 })
        ) {
            OutlinedButton(
                onClick = onPrevious,
                //modifier = Modifier.size(56.dp),
                shape = CircleShape,
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Previous",
                    modifier = Modifier.size(24.dp)
                )
                //Text("←", fontSize = 20.sp)
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Next/Complete button
        val buttonScale by animateFloatAsState(
            targetValue = 1f,
            animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
            label = "button_scale"
        )

        Button(
            onClick = if (isLastPage) onComplete else onNext,
            modifier = Modifier
                .scale(buttonScale)
                .height(56.dp)
                .widthIn(min = if (isLastPage) 140.dp else 120.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isLastPage) 
                    MaterialTheme.colorScheme.tertiary 
                else 
                    MaterialTheme.colorScheme.primary
            ),
            shape = RoundedCornerShape(28.dp)
        ) {
            AnimatedContent(
                targetState = isLastPage,
                transitionSpec = {
                    fadeIn(animationSpec = tween(200)) togetherWith 
                    fadeOut(animationSpec = tween(200))
                },
                label = "button_text"
            ) { isLast ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = if (isLast) "Get Started!" else "Next",
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp
                    )
                    
                    if (!isLast) {
                        Spacer(modifier = Modifier.width(8.dp))
                        //Text("→", fontSize = 16.sp)
                    } else {
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("✨", fontSize = 16.sp)
                    }
                }
            }
        }
    }
}