package com.example.travellikeasigma.data.repository

import android.util.Log
import com.example.travellikeasigma.data.remote.HotelApiService
import com.example.travellikeasigma.data.remote.dto.ReserveRequestDto
import com.example.travellikeasigma.data.remote.dto.toDomain
import com.example.travellikeasigma.domain.ApiHotel
import com.example.travellikeasigma.domain.ApiReservation
import com.example.travellikeasigma.domain.HotelRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HotelRepositoryImpl @Inject constructor(
    private val api: HotelApiService
) : HotelRepository {

    companion object {
        private const val TAG = "HotelRepository"
        const val GROUP_ID = "G13"
    }

    override suspend fun listHotels(groupId: String): List<ApiHotel> {
        Log.d(TAG, "listHotels: groupId=$groupId")
        return api.listHotels(groupId).map { it.toDomain() }
    }

    override suspend fun checkAvailability(
        groupId: String,
        startDate: String,
        endDate: String,
        city: String?
    ): List<ApiHotel> {
        Log.d(TAG, "checkAvailability: city=$city, $startDate → $endDate")
        return api.checkAvailability(groupId, startDate, endDate, city).availableHotels.map { it.toDomain() }
    }

    override suspend fun reserve(
        groupId: String,
        hotelId: String,
        roomId: String,
        startDate: String,
        endDate: String,
        guestName: String,
        guestEmail: String
    ): ApiReservation {
        Log.d(TAG, "reserve: hotel=$hotelId, room=$roomId, guest=$guestEmail")
        val request = ReserveRequestDto(hotelId, roomId, startDate, endDate, guestName, guestEmail)
        return api.reserve(groupId, request).reservation.toDomain()
    }

    override suspend fun listReservations(groupId: String, guestEmail: String?): List<ApiReservation> {
        Log.d(TAG, "listReservations: groupId=$groupId, email=$guestEmail")
        return api.listReservations(groupId, guestEmail).reservations.map { it.toDomain() }
    }

    override suspend fun cancelReservation(resId: String): Boolean {
        Log.d(TAG, "cancelReservation: resId=$resId")
        return try {
            api.cancelReservation(resId)
            Log.i(TAG, "cancelReservation: $resId cancelled successfully")
            true
        } catch (e: Exception) {
            Log.e(TAG, "cancelReservation: failed for $resId — ${e.message}")
            false
        }
    }
}
