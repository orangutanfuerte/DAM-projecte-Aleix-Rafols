package com.example.travellikeasigma.data.repository

import com.example.travellikeasigma.data.SharedPreferencesManager
import com.example.travellikeasigma.domain.UserPreferencesRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferencesRepositoryImpl @Inject constructor(
    private val sharedPreferencesManager: SharedPreferencesManager
) : UserPreferencesRepository {

    override fun isLoggedIn(): Boolean = sharedPreferencesManager.isLoggedIn

    override fun getLoggedInEmail(): String? = sharedPreferencesManager.loggedInEmail

    override fun getLoggedInUid(): String? = sharedPreferencesManager.loggedInUid

    override fun login(email: String, uid: String) {
        sharedPreferencesManager.isLoggedIn = true
        sharedPreferencesManager.loggedInEmail = email
        sharedPreferencesManager.loggedInUid = uid
    }

    override fun logout() {
        sharedPreferencesManager.clearSession()
    }

    override fun getThemeMode(): String = sharedPreferencesManager.themeMode

    override fun setThemeMode(mode: String) {
        sharedPreferencesManager.themeMode = mode
    }

    override fun isNotificationsEnabled(): Boolean = sharedPreferencesManager.notificationsEnabled

    override fun setNotificationsEnabled(enabled: Boolean) {
        sharedPreferencesManager.notificationsEnabled = enabled
    }

    override fun getLanguage(): String = sharedPreferencesManager.language

    override fun setLanguage(language: String) {
        sharedPreferencesManager.language = language
    }
}
