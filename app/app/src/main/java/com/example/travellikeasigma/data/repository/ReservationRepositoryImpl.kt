package com.example.travellikeasigma.data.repository

import com.example.travellikeasigma.data.room.ReservationDao
import com.example.travellikeasigma.data.room.toDomain
import com.example.travellikeasigma.data.room.toEntity
import com.example.travellikeasigma.domain.LocalReservation
import com.example.travellikeasigma.domain.ReservationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReservationRepositoryImpl @Inject constructor(
    private val dao: ReservationDao
) : ReservationRepository {

    override fun getReservations(userId: String): Flow<List<LocalReservation>> =
        dao.getByUser(userId).map { list -> list.map { it.toDomain() } }

    override suspend fun saveReservation(reservation: LocalReservation, userId: String) {
        dao.insert(reservation.toEntity(userId))
    }

    override suspend fun deleteReservation(reservationId: String) {
        dao.deleteById(reservationId)
    }
}
