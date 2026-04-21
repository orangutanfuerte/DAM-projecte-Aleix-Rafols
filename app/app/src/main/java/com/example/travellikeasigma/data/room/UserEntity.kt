package com.example.travellikeasigma.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val userId: String,
    val email: String,
    val username: String,
    val dateOfBirth: String,
    val language: String,
    val theme: String,
    val notificationsEnabled: Boolean
)
