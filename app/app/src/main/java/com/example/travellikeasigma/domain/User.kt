package com.example.travellikeasigma.model

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
    val preferences: Preferences = Preferences(),
    val trips: List<Trip> = emptyList()
) {
    fun createTrip(trip: Trip) { /* @TODO */ }
    fun removeTrip(trip: Trip) { /* @TODO */ }
    fun updatePreferences(preferences: Preferences) { /* @TODO */ }
}
