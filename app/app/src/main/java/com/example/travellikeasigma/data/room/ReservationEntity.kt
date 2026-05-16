package com.example.travellikeasigma.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reservations")
data class ReservationEntity(
    @PrimaryKey val id: String,
    val hotelId: String,
    val hotelName: String,
    val hotelAddress: String,
    val hotelRating: Int,
    val hotelImageUrl: String,
    val roomId: String,
    val roomType: String,
    val pricePerNight: Double,
    val roomImages: String,
    val startDate: String,
    val endDate: String,
    val guestName: String,
    val guestEmail: String,
    val persons: Int,
    val userId: String
)
