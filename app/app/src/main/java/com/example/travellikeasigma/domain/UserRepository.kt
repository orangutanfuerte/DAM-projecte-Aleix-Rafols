package com.example.travellikeasigma.domain

interface UserRepository {
    suspend fun saveUser(user: User)
    suspend fun getUserById(uid: String): User?
}
