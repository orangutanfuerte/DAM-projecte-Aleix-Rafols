package com.example.travellikeasigma.domain

interface AuthRepository {
    fun getCurrentUser(): AuthUser?
    suspend fun login(email: String, password: String): AuthUser
    suspend fun register(email: String, password: String, displayName: String): AuthUser
    suspend fun reloadCurrentUser()
    suspend fun sendEmailVerification()
    suspend fun sendPasswordResetEmail(email: String)
    fun signOut()
}
