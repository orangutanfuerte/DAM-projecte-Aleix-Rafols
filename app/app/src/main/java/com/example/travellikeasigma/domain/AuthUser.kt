package com.example.travellikeasigma.domain

data class AuthUser(
    val uid: String,
    val email: String,
    val isEmailVerified: Boolean,
    val displayName: String?
)
