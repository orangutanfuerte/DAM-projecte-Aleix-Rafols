package com.example.travellikeasigma.data.repository

import android.util.Log
import com.example.travellikeasigma.data.fakeDB.FakeTripDataSource
import com.example.travellikeasigma.domain.ItineraryActivity
import com.example.travellikeasigma.domain.Trip
import com.example.travellikeasigma.domain.TripRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TripRepositoryImpl @Inject constructor(
    private val dataSource: FakeTripDataSource
) : TripRepository {

    companion object {
        private const val TAG = "TripRepositoryImpl"
    }

    override fun getAllTrips(): List<Trip> {
        val trips = dataSource.getAllTrips()
        Log.d(TAG, "getAllTrips: returning ${trips.size} trips")
        return trips
    }

    override fun getTripById(id: Int): Trip? {
        val trip = dataSource.getTripById(id)
        if (trip == null) {
            Log.w(TAG, "getTripById: no trip found with id=$id")
        }
        return trip
    }

    override fun addTrip(trip: Trip) {
        Log.d(TAG, "addTrip: id=${trip.id}, name='${trip.name}'")
        dataSource.addTrip(trip)
    }

    override fun removeTrip(tripId: Int) {
        Log.d(TAG, "removeTrip: id=$tripId")
        dataSource.removeTrip(tripId)
    }

    override fun addActivity(tripId: Int, activity: ItineraryActivity) {
        Log.d(TAG, "addActivity: tripId=$tripId, activityTitle='${activity.title}'")
        dataSource.addActivity(tripId, activity)
    }

    override fun updateActivity(tripId: Int, activity: ItineraryActivity) {
        Log.d(TAG, "updateActivity: tripId=$tripId, activityId=${activity.id}")
        dataSource.updateActivity(tripId, activity)
    }

    override fun removeActivity(tripId: Int, activityId: Int) {
        Log.d(TAG, "removeActivity: tripId=$tripId, activityId=$activityId")
        dataSource.removeActivity(tripId, activityId)
    }
}
