package com.example.devops.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.navigation.compose.rememberNavController
import com.example.devops.navigation.NavigationGraph
import com.example.devops.ui.features.auth.login.presentation.viewModel.LoginViewModel
import com.example.devops.ui.theme.DevOpsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Handle OAuth callback if this activity was opened via deep link
        handleOAuthCallback(intent)

        enableEdgeToEdge()
        setContent {

            val navController = rememberNavController()

            DevOpsTheme {

                NavigationGraph(
                    navController = navController
                )
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleOAuthCallback(intent)
    }

    private fun handleOAuthCallback(intent: Intent) {
        val uri = intent.data

        if (uri != null && uri.scheme == "devops" && uri.host == "oauth") {
            val code = uri.getQueryParameter("code")
            val state = uri.getQueryParameter("state")
            val error = uri.getQueryParameter("error")

            when {
                error != null -> {
                    loginViewModel.setOAuthError("OAuth error: $error")
                }

                code != null && state != null -> {
                    loginViewModel.handleOAuthCallback(code, state)
                }

                else -> {
                    loginViewModel.setOAuthError("Invalid OAuth response")
                }
            }
        }
    }
}