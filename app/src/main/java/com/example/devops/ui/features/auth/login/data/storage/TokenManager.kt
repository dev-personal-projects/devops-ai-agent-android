package com.example.devops.ui.features.auth.login.data.storage

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

// Manages secure storage for access tokens and user information using EncryptedSharedPreferences.
@Singleton
class TokenManager @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPreferences: SharedPreferences = EncryptedSharedPreferences.create(
        context,
        "auth_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    companion object {
        private const val KEY_ACCESS_TOKEN = "access_token"
        private const val KEY_USER_INFO = "user_info"
    }

    fun saveAccessToken(token: String) {
        sharedPreferences.edit()
            .putString(KEY_ACCESS_TOKEN, token)
            .apply()
    }

    fun getAccessToken(): String? {
        return sharedPreferences.getString(KEY_ACCESS_TOKEN, null)
    }

    fun saveUserInfo(userJson: String) {
        sharedPreferences.edit()
            .putString(KEY_USER_INFO, userJson)
            .apply()
    }

    fun getUserInfo(): String? {
        return sharedPreferences.getString(KEY_USER_INFO, null)
    }

    fun clearTokens() {
        sharedPreferences.edit()
            .clear()
            .apply()
    }

    fun isLoggedIn(): Boolean {
        return getAccessToken() != null
    }
}