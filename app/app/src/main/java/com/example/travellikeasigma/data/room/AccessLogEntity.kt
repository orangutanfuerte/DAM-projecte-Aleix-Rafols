package com.example.travellikeasigma.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "access_log")
data class AccessLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: String,
    val action: String,
    val dateTime: String
)
