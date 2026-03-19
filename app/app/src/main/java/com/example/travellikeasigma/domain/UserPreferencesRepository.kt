package com.example.travellikeasigma.domain

interface UserPreferencesRepository {
    fun isLoggedIn(): Boolean
    fun getLoggedInEmail(): String?
    fun login(email: String)
    fun logout()
    fun getThemeMode(): String
    fun setThemeMode(mode: String)
    fun isNotificationsEnabled(): Boolean
    fun setNotificationsEnabled(enabled: Boolean)
    fun getLanguage(): String
    fun setLanguage(language: String)
}
