package com.example.travellikeasigma.domain

data class ApiReservation(
    val id: String,
    val hotelId: String,
    val roomId: String,
    val startDate: String,
    val endDate: String,
    val guestName: String,
    val guestEmail: String,
    val hotel: ApiHotel? = null,
    val room: ApiRoom? = null
)
