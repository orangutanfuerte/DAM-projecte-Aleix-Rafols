package com.example.travellikeasigma.domain

data class Preferences(
    val notificationsEnabled: Boolean = true,
    val theme: String = "system",
    val preferredLanguage: String = "en"
)

data class User(
    val id: Int,
    val email: String,
    val password: String,
    val preferences: Preferences = Preferences()
)

val sampleUser = User(
    id       = 1,
    email    = "sofia.martinez@email.com",
    password = "1234"
)
