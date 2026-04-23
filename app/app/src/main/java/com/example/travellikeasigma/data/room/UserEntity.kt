package com.example.travellikeasigma.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val userId: String,
    val name: String,
    val email: String,
    val username: String,
    val dateOfBirth: String,
    val phone: String,
    val address: String,
    val country: String,
    val acceptsReceiveEmails: Boolean,
    val language: String,
    val theme: String,
    val notificationsEnabled: Boolean
)
