package com.example.travellikeasigma.domain

class Authentication {
    fun login(email: String, password: String) { /* @TODO */ }
    fun logout()                               { /* @TODO */ }
    fun resetPassword(email: String)           { /* @TODO */ }
}

data class Preferences(
    val notificationsEnabled: Boolean = true,
    val theme: String = "system",
    val preferredLanguage: String = "en"
)

data class User(
    val id: Int,
    val email: String,
    val password: String,
    val authentication: Authentication = Authentication(),
    val preferences: Preferences = Preferences()
) {
    fun updatePreferences(preferences: Preferences) { /* @TODO */ }
}

val sampleUser = User(
    id    = 1,
    email = "sigma@travel.com",
    password = ""
)
