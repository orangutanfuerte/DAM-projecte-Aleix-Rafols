package com.example.travellikeasigma.domain

interface UserRepository {
    suspend fun saveUser(user: User)
    suspend fun getUserById(uid: String): User?
    suspend fun updateUser(user: User)
    suspend fun isUsernameTaken(username: String): Boolean
}
