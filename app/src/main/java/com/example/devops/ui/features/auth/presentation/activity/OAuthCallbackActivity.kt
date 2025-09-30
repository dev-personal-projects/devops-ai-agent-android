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
import android.util.Log

@AndroidEntryPoint
class OAuthCallbackActivity : ComponentActivity() {

    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleOAuthCallback()
    }

    private fun handleOAuthCallback() {
        val uri: Uri? = intent.data
        Log.d("OAuthCallback", "Received URI: $uri")
        
        // Log all query parameters to see what we're actually receiving
        uri?.let { u ->
            Log.d("OAuthCallback", "URI scheme: ${u.scheme}, host: ${u.host}")
            Log.d("OAuthCallback", "All query parameters:")
            u.queryParameterNames.forEach { param ->
                Log.d("OAuthCallback", "  $param = ${u.getQueryParameter(param)}")
            }
        }

        if (uri != null && uri.scheme == "devops" && uri.host == "oauth") {
            // Extract all possible parameters
            val accessToken = uri.getQueryParameter("access_token")
            val refreshToken = uri.getQueryParameter("refresh_token")
            val userId = uri.getQueryParameter("user_id")
            val code = uri.getQueryParameter("code")
            val state = uri.getQueryParameter("state")
            val error = uri.getQueryParameter("error")
            
            Log.d("OAuthCallback", "Extracted parameters:")
            Log.d("OAuthCallback", "  access_token: ${accessToken?.take(10)}...")
            Log.d("OAuthCallback", "  refresh_token: ${refreshToken?.take(10)}...")
            Log.d("OAuthCallback", "  user_id: $userId")
            Log.d("OAuthCallback", "  code: ${code?.take(10)}...")
            Log.d("OAuthCallback", "  state: ${state?.take(10)}...")
            Log.d("OAuthCallback", "  error: $error")

            when {
                error != null -> {
                    Log.e("OAuthCallback", "OAuth error: $error")
                    navigateToMainWithError("OAuth failed: $error")
                }
                accessToken != null -> {
                    Log.d("OAuthCallback", "Received tokens from backend redirect")
                    storeTokensAndUpdateAuthState(accessToken, refreshToken, userId)
                    navigateToMain()
                }
                code != null && state != null -> {
                    Log.d("OAuthCallback", "Received OAuth code, need to exchange for tokens")
                    authViewModel.handleEvent(AuthEvent.HandleOAuthCallback(code, state))
                    finish()
                }
                else -> {
                    Log.e("OAuthCallback", "No valid parameters found")
                    navigateToMainWithError("Invalid authentication state. Please try again.")
                }
            }
        } else {
            Log.e("OAuthCallback", "Invalid URI scheme or host. Expected devops://oauth")
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

    private fun storeTokensAndUpdateAuthState(accessToken: String, refreshToken: String?, userId: String?) {
        Log.d("OAuthCallback", "Storing tokens and updating auth state")
        authViewModel.handleEvent(AuthEvent.HandleDirectTokens(accessToken, refreshToken, userId))
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