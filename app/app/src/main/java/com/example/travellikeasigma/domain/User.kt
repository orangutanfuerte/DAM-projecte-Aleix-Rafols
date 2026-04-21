package com.example.travellikeasigma.domain

data class Preferences(
    val language: String = "en",
    val theme: String = "system",
    val notificationsEnabled: Boolean = true
)

data class User(
    val uid: String,
    val name: String,
    val username: String,
    val email: String,
    val dateOfBirth: String,
    val preferences: Preferences = Preferences()
)
