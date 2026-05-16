package com.example.travellikeasigma.domain

import kotlinx.coroutines.flow.Flow

interface ReservationRepository {
    fun getReservations(userId: String): Flow<List<LocalReservation>>
    suspend fun saveReservation(reservation: LocalReservation, userId: String)
    suspend fun deleteReservation(reservationId: String)
}
