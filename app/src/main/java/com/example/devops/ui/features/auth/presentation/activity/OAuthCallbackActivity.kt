package com.example.devops.ui.features.auth.presentation.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import com.example.devops.MainActivity
import com.example.devops.ui.features.auth.presentation.event.AuthEvent
import com.example.devops.ui.features.auth.presentation.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OAuthCallbackActivity : ComponentActivity() {

    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleOAuthCallback()
    }

    private fun handleOAuthCallback() {
        val uri: Uri? = intent.data

        if (uri != null && uri.scheme == "devops" && uri.host == "oauth") {
            val accessToken = uri.getQueryParameter("access_token")
            val refreshToken = uri.getQueryParameter("refresh_token")
            val userId = uri.getQueryParameter("user_id")
            val error = uri.getQueryParameter("error")

            when {
                error != null -> {
                    navigateToMainWithError("OAuth failed: $error")
                }
                accessToken != null && userId != null -> {
                    // Store tokens and navigate to home
                    // For now, just navigate to main - token storage will be handled later
                    navigateToMain()
                }
                else -> {
                    navigateToMainWithError("Invalid OAuth callback")
                }
            }
        } else {
            navigateToMainWithError("Invalid OAuth callback URL")
        }
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(intent)
        finish()
    }

    private fun navigateToMainWithError(errorMessage: String) {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            putExtra("oauth_error", errorMessage)
        }
        startActivity(intent)
        finish()
    }
}