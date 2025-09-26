package com.example.devops.ui.features.onBoarding.data.model

data class OnboardingPage(
    val title: String,
    val description: String,
    val emoji: String,
    val backgroundColor: Long,
    val accentColor: Long
)


val onboardingPages = listOf(
    OnboardingPage(
        title = "Welcome to DevOps Hub! 👋",
        description = "Your all-in-one platform to streamline development workflows, manage repositories, and collaborate with your amazing team!",
        emoji = "🚀",
        backgroundColor = 0xFF3B82F6,
        accentColor = 0xFFF9A8D4
    ),
    OnboardingPage(
        title = "Connect & Collaborate ✨",
        description = "Seamlessly integrate with GitHub, track your projects, monitor CI/CD pipelines, and work together like never before!",
        emoji = "🤝",
        backgroundColor = 0xFF10B981,
        accentColor = 0xFFDCE7FF
    ),
    OnboardingPage(
        title = "Ship Faster, Ship Better 🎯",
        description = "Deploy with confidence using our powerful tools, automated workflows, and real-time monitoring. Let's build something amazing!",
        emoji = "⚡",
        backgroundColor = 0xFFEC4899,
        accentColor = 0xFFFFE4B5
    )
)