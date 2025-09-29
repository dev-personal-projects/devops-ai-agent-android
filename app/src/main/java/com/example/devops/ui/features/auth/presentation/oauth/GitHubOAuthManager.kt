package com.example.devops.ui.features.auth.presentation.oauth

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import com.example.devops.ui.features.auth.domain.repository.AuthRepository
import com.example.devops.ui.features.auth.domain.model.AuthResult
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GitHubOAuthManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val authRepository: AuthRepository
) {

    suspend fun startOAuthFlow() {
        when (val result = authRepository.initiateGitHubOAuth()) {
            is AuthResult.Success -> {
                openCustomTab(result.data)
            }
            is AuthResult.Error -> {
                throw result.exception ?: Exception(result.message)
            }
        }
    }

    private fun openCustomTab(url: String) {
        val customTabsIntent = CustomTabsIntent.Builder()
            .setShowTitle(true)
            .setColorScheme(CustomTabsIntent.COLOR_SCHEME_SYSTEM)
            .build()

        val intent = customTabsIntent.intent.apply {
            data = Uri.parse(url)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        context.startActivity(intent)
    }
}