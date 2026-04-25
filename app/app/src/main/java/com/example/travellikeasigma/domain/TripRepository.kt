package com.example.travellikeasigma.domain

import kotlinx.coroutines.flow.Flow

interface TripRepository {
    fun getAllTrips(userId: String): Flow<List<Trip>>
    fun getTripById(id: Int): Trip?
    suspend fun addTrip(trip: Trip, userId: String)
    suspend fun removeTrip(tripId: Int)
    suspend fun addActivity(tripId: Int, activity: ItineraryActivity)
    suspend fun updateActivity(tripId: Int, activity: ItineraryActivity)
    suspend fun removeActivity(tripId: Int, activityId: Int)
    suspend fun seedIfEmpty(userId: String)
}
