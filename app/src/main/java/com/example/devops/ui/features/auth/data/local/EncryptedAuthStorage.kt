package com.example.devops.ui.features.auth.data.local

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.devops.ui.features.auth.domain.model.AuthState
import com.example.devops.ui.features.auth.data.local.model.UserStorageModel
import com.example.devops.ui.features.auth.data.local.model.AuthTokensStorageModel
import com.example.devops.ui.features.auth.data.local.model.toStorageModel
import com.example.devops.core.exceptions.AuthStorageException
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.google.gson.Gson
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class EncryptedAuthStorage @Inject constructor(
    @ApplicationContext private val context: Context
) : LocalAuthDataSource {

    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val prefs: SharedPreferences = EncryptedSharedPreferences.create(
        context,
        "auth_secure_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    private val gson = Gson()

    private val _authStateFlow = MutableStateFlow(AuthState())

    init {
        // Initialize flow with stored state
        kotlin.runCatching {
            val storedState = getStoredAuthState()
            _authStateFlow.value = storedState
        }
    }

    override suspend fun saveAuthState(authState: AuthState) {
        try {
            val userJson = authState.user?.let { gson.toJson(it.toStorageModel()) }
            val tokensJson = authState.tokens?.let { gson.toJson(it.toStorageModel()) }

            prefs.edit()
                .putBoolean(KEY_IS_AUTHENTICATED, authState.isAuthenticated)
                .putString(KEY_USER, userJson)
                .putString(KEY_TOKENS, tokensJson)
                .apply()

            _authStateFlow.value = authState
        } catch (e: Exception) {
            throw AuthStorageException("Failed to save auth state", e)
        }
    }

    override suspend fun getAuthState(): AuthState? {
        return try {
            getStoredAuthState()
        } catch (e: Exception) {
            null
        }
    }

    private fun getStoredAuthState(): AuthState {
        val isAuthenticated = prefs.getBoolean(KEY_IS_AUTHENTICATED, false)

        if (!isAuthenticated) {
            return AuthState(isAuthenticated = false)
        }

        val userJson = prefs.getString(KEY_USER, null)
        val tokensJson = prefs.getString(KEY_TOKENS, null)

        val user = userJson?.let {
            gson.fromJson(it, UserStorageModel::class.java).toDomain()
        }
        val tokens = tokensJson?.let {
            gson.fromJson(it, AuthTokensStorageModel::class.java).toDomain()
        }

        return AuthState(
            isAuthenticated = isAuthenticated,
            user = user,
            tokens = tokens
        )
    }

    override suspend fun clearAuthState() {
        prefs.edit().clear().apply()
        _authStateFlow.value = AuthState(isAuthenticated = false)
    }

    override fun observeAuthState(): Flow<AuthState> = _authStateFlow.asStateFlow()

    companion object {
        private const val KEY_IS_AUTHENTICATED = "is_authenticated"
        private const val KEY_USER = "user"
        private const val KEY_TOKENS = "tokens"
    }
}