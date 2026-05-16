package com.example.travellikeasigma.domain

data class LocalReservation(
    val id: String,
    val hotelId: String,
    val hotelName: String,
    val hotelAddress: String,
    val hotelRating: Int,
    val hotelImageUrl: String,
    val roomId: String,
    val roomType: String,
    val pricePerNight: Double,
    val roomImages: List<String>,
    val startDate: String,
    val endDate: String,
    val guestName: String,
    val guestEmail: String,
    val persons: Int
)
