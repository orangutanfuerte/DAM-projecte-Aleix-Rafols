package com.example.travellikeasigma.domain

interface HotelRepository {
    suspend fun listHotels(groupId: String): List<ApiHotel>
    suspend fun checkAvailability(groupId: String, startDate: String, endDate: String, city: String?): List<ApiHotel>
    suspend fun reserve(groupId: String, hotelId: String, roomId: String, startDate: String, endDate: String, guestName: String, guestEmail: String): ApiReservation
    suspend fun listReservations(groupId: String, guestEmail: String?): List<ApiReservation>
    suspend fun cancelReservation(resId: String): Boolean
}
