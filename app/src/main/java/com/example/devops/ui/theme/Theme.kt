    package com.example.devops.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = DarkBlue80,
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = DarkBlue20,
    onPrimaryContainer = Color(0xFFDCE7FF),

    secondary = GitHubGray80,
    onSecondary = Color(0xFF000000),
    secondaryContainer = GitHubGray20,
    onSecondaryContainer = Color(0xFFF3F4F6),

    tertiary = CutePink80,
    onTertiary = Color(0xFF000000),
    tertiaryContainer = CutePink20,
    onTertiaryContainer = Color(0xFFFDF2F8),

    error = ErrorRed,
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFF7F1D1D),
    onErrorContainer = Color(0xFFFEF2F2),

    background = SurfaceDark,
    onBackground = OnSurfaceDark,

    surface = SurfaceDark,
    onSurface = OnSurfaceDark,
    surfaceVariant = CardDark,
    onSurfaceVariant = Color(0xFF8B949E),

    outline = Color(0xFF30363D),
    outlineVariant = Color(0xFF21262D),

    inverseSurface = Color(0xFFF0F6FC),
    inverseOnSurface = Color(0xFF0D1117),
    inversePrimary = DarkBlue40,

    surfaceTint = DarkBlue80,
    scrim = Color(0xFF000000)
)

private val LightColorScheme = lightColorScheme(
    primary = DarkBlue40,
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFDCE7FF),
    onPrimaryContainer = DarkBlue20,

    secondary = GitHubGray40,
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFF3F4F6),
    onSecondaryContainer = Color(0xFF1F2937),

    tertiary = CutePink40,
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFFDF2F8),
    onTertiaryContainer = CutePink20,

    error = ErrorRed,
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFEF2F2),
    onErrorContainer = Color(0xFF7F1D1D),

    background = SurfaceLight,
    onBackground = OnSurfaceLight,

    surface = SurfaceLight,
    onSurface = OnSurfaceLight,
    surfaceVariant = CardLight,
    onSurfaceVariant = Color(0xFF64748B),

    outline = Color(0xFFE2E8F0),
    outlineVariant = Color(0xFFF1F5F9),

    inverseSurface = Color(0xFF0F172A),
    inverseOnSurface = Color(0xFFF8FAFC),
    inversePrimary = DarkBlue80,

    surfaceTint = DarkBlue40,
    scrim = Color(0xFF000000)
)

// Additional custom colors for specific DevOps features
object DevOpsColors {
    // Status Colors
    val Success = SuccessGreen
    val Warning = WarningOrange
    val Error = ErrorRed
    val Info = DarkBlue80

    // Card backgrounds for different states
    val SuccessContainer = Color(0xFFD1FAE5)
    val WarningContainer = Color(0xFFFEF3C7)
    val ErrorContainer = Color(0xFFFEE2E2)
    val InfoContainer = Color(0xFFDCE7FF)

    // GitHub specific colors
    val GitHubGreen = Color(0xFF238636)      // For merge/success
    val GitHubOrange = Color(0xFFDA863E)     // For pending/warning
    val GitHubRed = Color(0xFFDA3633)        // For failed/error
    val GitHubPurple = Color(0xFF8250DF)     // For in-progress
}

@Composable
fun DevOpsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> DarkColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}