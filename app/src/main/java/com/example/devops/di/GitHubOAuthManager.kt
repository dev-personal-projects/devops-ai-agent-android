package com.example.devops.di

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import com.example.devops.ui.features.auth.login.data.repo.AuthRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Service class responsible for initiating the client-side OAuth flow using Custom Tabs.
 */
@Singleton
class GitHubOAuthManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val authRepository: AuthRepository
) {

    suspend fun startOAuthFlow() {
        authRepository.getGitHubAuthUrl().fold(
            onSuccess = { oauthResponse ->
                // The URL obtained from the backend is the full authorization link
                openCustomTab(oauthResponse.authorization_url)
            },
            onFailure = { error ->
                // Throw the error to be caught by the ViewModel
                throw error
            }
        )
    }

    private fun openCustomTab(url: String) {
        val customTabsIntent = CustomTabsIntent.Builder()
            .setShowTitle(true)
            .setColorScheme(CustomTabsIntent.COLOR_SCHEME_SYSTEM)
            .build()

        val intent = customTabsIntent.intent.apply {
            data = Uri.parse(url)
            // FLAG_ACTIVITY_NEW_TASK is mandatory when starting an Activity from a non-Activity context (like a Singleton DI class)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        context.startActivity(intent)
    }
}
