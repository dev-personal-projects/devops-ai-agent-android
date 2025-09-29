package com.example.devops.ui.features.onboarding.presentation.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

data class OnboardingPage(
    val title: String,
    val description: String,
    val emoji: String
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(
    onCompleted: () -> Unit
) {
    val pages = listOf(
        OnboardingPage(
            title = "Streamline Your Workflow",
            description = "Automate your development pipeline with intelligent CI/CD management and real-time monitoring.",
            emoji = "🚀"
        ),
        OnboardingPage(
            title = "GitHub Integration",
            description = "Seamlessly connect with your GitHub repositories and manage pull requests, issues, and deployments.",
            emoji = "🐙"
        ),
        OnboardingPage(
            title = "Smart Analytics",
            description = "Get insights into your development process with detailed analytics and performance metrics.",
            emoji = "📊"
        )
    )

    val pagerState = rememberPagerState(pageCount = { pages.size })
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            OnboardingPageContent(
                page = pages[page],
                modifier = Modifier.fillMaxSize()
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pages.size) { index ->
                val isSelected = pagerState.currentPage == index
                Box(
                    modifier = Modifier
                        .size(if (isSelected) 12.dp else 8.dp)
                        .clip(CircleShape)
                        .background(
                            if (isSelected) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                        )
                )
                if (index < pages.size - 1) {
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = onCompleted) {
                Text("Skip")
            }

            Button(
                onClick = {
                    if (pagerState.currentPage < pages.size - 1) {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    } else {
                        onCompleted()
                    }
                },
                modifier = Modifier.height(48.dp)
            ) {
                Text(
                    if (pagerState.currentPage < pages.size - 1) "Next" else "Get Started"
                )
            }
        }
    }
}

@Composable
fun OnboardingPageContent(
    page: OnboardingPage,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = page.emoji,
            style = MaterialTheme.typography.displayLarge,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        Text(
            text = page.title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = page.description,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
        )
    }
}