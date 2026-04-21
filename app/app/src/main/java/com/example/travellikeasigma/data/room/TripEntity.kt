package com.example.travellikeasigma.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trips")
data class TripEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val destinationId: Int,
    val hotelId: Int,
    val startDate: String,
    val endDate: String,
    val persons: Int,
    val heroColor: Long,
    val userId: String
)
