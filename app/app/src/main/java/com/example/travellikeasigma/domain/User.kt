package com.example.travellikeasigma.domain

data class Preferences(
    val language: String = "en",
    val theme: String = "system",
    val notificationsEnabled: Boolean = true
)

data class User(
    val id: Int,
    val name: String,
    val username: String,
    val email: String,
    val password: String,
    val dateOfBirth: String,
    val preferences: Preferences = Preferences()
)

val sampleUser = User(
    id          = 1,
    name        = "Sofia Martínez",
    username    = "sofiamartinez",
    email       = "sofia.martinez@email.com",
    password    = "1234",
    dateOfBirth = "24/05/2004"
)
