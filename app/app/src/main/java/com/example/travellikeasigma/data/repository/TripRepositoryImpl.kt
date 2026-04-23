package com.example.travellikeasigma.data.repository

import android.util.Log
import com.example.travellikeasigma.data.fakeDB.FakeTripDataSource
import com.example.travellikeasigma.domain.ItineraryActivity
import com.example.travellikeasigma.domain.Trip
import com.example.travellikeasigma.domain.TripRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TripRepositoryImpl @Inject constructor(
    private val dataSource: FakeTripDataSource
) : TripRepository {

    companion object {
        private const val TAG = "TripRepositoryImpl"
    }

    override fun getAllTrips(userId: String): Flow<List<Trip>> = dataSource.getAllTrips(userId)

    override fun getTripById(id: Int): Trip? {
        val trip = dataSource.getTripById(id)
        if (trip == null) Log.w(TAG, "getTripById: no trip found with id=$id")
        return trip
    }

    override suspend fun addTrip(trip: Trip, userId: String) {
        Log.d(TAG, "addTrip: id=${trip.id}, name='${trip.name}'")
        dataSource.addTrip(trip, userId)
    }

    override suspend fun removeTrip(tripId: Int) {
        Log.d(TAG, "removeTrip: id=$tripId")
        dataSource.removeTrip(tripId)
    }

    override suspend fun addActivity(tripId: Int, activity: ItineraryActivity) {
        Log.d(TAG, "addActivity: tripId=$tripId, activityTitle='${activity.title}'")
        dataSource.addActivity(tripId, activity)
    }

    override suspend fun updateActivity(tripId: Int, activity: ItineraryActivity) {
        Log.d(TAG, "updateActivity: tripId=$tripId, activityId=${activity.id}")
        dataSource.updateActivity(tripId, activity)
    }

    override suspend fun removeActivity(tripId: Int, activityId: Int) {
        Log.d(TAG, "removeActivity: tripId=$tripId, activityId=$activityId")
        dataSource.removeActivity(tripId, activityId)
    }

    override suspend fun seedIfEmpty(userId: String, email: String) {
        dataSource.seedIfEmpty(userId)
    }
}
