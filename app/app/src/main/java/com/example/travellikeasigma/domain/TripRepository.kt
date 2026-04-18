package com.example.travellikeasigma.domain

import kotlinx.coroutines.flow.Flow

interface TripRepository {
    fun getAllTrips(): Flow<List<Trip>>
    fun getTripById(id: Int): Trip?
    suspend fun addTrip(trip: Trip)
    suspend fun removeTrip(tripId: Int)
    suspend fun addActivity(tripId: Int, activity: ItineraryActivity)
    suspend fun updateActivity(tripId: Int, activity: ItineraryActivity)
    suspend fun removeActivity(tripId: Int, activityId: Int)
}
