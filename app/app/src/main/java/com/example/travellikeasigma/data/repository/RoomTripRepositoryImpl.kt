package com.example.travellikeasigma.data.repository

import android.util.Log
import com.example.travellikeasigma.data.room.ItineraryActivityDao
import com.example.travellikeasigma.data.room.toDomain
import com.example.travellikeasigma.data.room.toEntity
import com.example.travellikeasigma.data.room.TripDao
import com.example.travellikeasigma.domain.ItineraryActivity
import com.example.travellikeasigma.domain.Trip
import com.example.travellikeasigma.domain.TripRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoomTripRepositoryImpl @Inject constructor(
    private val tripDao: TripDao,
    private val activityDao: ItineraryActivityDao
) : TripRepository {

    companion object {
        private const val TAG = "RoomTripRepository"
    }

    @Volatile
    private var tripsCache: List<Trip> = emptyList()

    override fun getAllTrips(userId: String): Flow<List<Trip>> =
        tripDao.getAllTripsWithActivities(userId)
            .map { rows -> rows.map { it.toDomain() } }
            .onEach { tripsCache = it }

    override fun getTripById(id: Int): Trip? = tripsCache.find { it.id == id }

    override suspend fun addTrip(trip: Trip, userId: String) {
        Log.d(TAG, "addTrip: name='${trip.name}', userId=$userId")
        tripDao.insertTrip(trip.toEntity(userId))
    }

    override suspend fun removeTrip(tripId: Int) {
        Log.d(TAG, "removeTrip: id=$tripId")
        tripDao.deleteTripById(tripId)
    }

    override suspend fun addActivity(tripId: Int, activity: ItineraryActivity) {
        Log.d(TAG, "addActivity: '${activity.title}' → tripId=$tripId")
        activityDao.insertActivity(activity.toEntity(tripId))
    }

    override suspend fun updateActivity(tripId: Int, activity: ItineraryActivity) {
        Log.d(TAG, "updateActivity: id=${activity.id}, '${activity.title}'")
        activityDao.updateActivity(activity.toEntity(tripId))
    }

    override suspend fun removeActivity(tripId: Int, activityId: Int) {
        Log.d(TAG, "removeActivity: id=$activityId from tripId=$tripId")
        activityDao.deleteActivityById(activityId)
    }

    override suspend fun isTripNameTaken(userId: String, name: String): Boolean {
        val taken = tripDao.countTripsWithName(userId, name) > 0
        if (taken) Log.w(TAG, "isTripNameTaken: '$name' already exists for userId=$userId")
        return taken
    }

    override suspend fun seedIfEmpty(userId: String) { /* no-op */ }
}
