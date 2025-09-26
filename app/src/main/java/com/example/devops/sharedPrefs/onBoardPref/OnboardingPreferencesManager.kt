package com.example.devops.sharedPrefs.onBoardPref

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OnboardingPreferencesManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val prefs: SharedPreferences = context.getSharedPreferences(
        "onboarding_prefs", 
        Context.MODE_PRIVATE
    )

    companion object {
        private const val KEY_ONBOARDING_COMPLETED = "onboarding_completed"
    }

    suspend fun hasSeenOnboarding(): Boolean = withContext(Dispatchers.IO) {
        prefs.getBoolean(KEY_ONBOARDING_COMPLETED, false)
    }

    suspend fun setOnboardingCompleted() = withContext(Dispatchers.IO) {
        prefs.edit()
            .putBoolean(KEY_ONBOARDING_COMPLETED, true)
            .apply()
    }

    suspend fun clearOnboarding() = withContext(Dispatchers.IO) {
        prefs.edit()
            .putBoolean(KEY_ONBOARDING_COMPLETED, false)
            .apply()
    }
}