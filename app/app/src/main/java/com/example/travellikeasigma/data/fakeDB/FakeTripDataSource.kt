package com.example.travellikeasigma.data.fakeDB

import android.util.Log
import com.example.travellikeasigma.domain.ItineraryActivity
import com.example.travellikeasigma.domain.Trip
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeTripDataSource @Inject constructor() {

    private val _trips = MutableStateFlow(emptyList<Trip>())

    fun getAllTrips(userId: String = ""): Flow<List<Trip>> = _trips.asStateFlow()

    fun getTripById(id: Int): Trip? = _trips.value.find { it.id == id }

    suspend fun seedIfEmpty(userId: String) { /* no-op for in-memory fake */ }

    fun addTrip(trip: Trip, userId: String = "") {
        Log.d(TAG, "addTrip: id=${trip.id}, name='${trip.name}'")
        _trips.update { it + trip }
    }

    fun removeTrip(tripId: Int) {
        val exists = _trips.value.any { it.id == tripId }
        if (exists) {
            _trips.update { it.filter { t -> t.id != tripId } }
            Log.d(TAG, "removeTrip: removed trip id=$tripId")
        } else {
            Log.w(TAG, "removeTrip: no trip found with id=$tripId")
        }
    }

    fun addActivity(tripId: Int, activity: ItineraryActivity) {
        _trips.update { trips ->
            trips.map { trip ->
                if (trip.id == tripId) {
                    Log.d(TAG, "addActivity: added '${activity.title}' to trip id=$tripId")
                    trip.copy(activities = trip.activities + activity)
                } else trip
            }
        }
    }

    fun updateActivity(tripId: Int, activity: ItineraryActivity) {
        _trips.update { trips ->
            trips.map { trip ->
                if (trip.id == tripId) {
                    Log.d(TAG, "updateActivity: updated activity id=${activity.id} in trip id=$tripId")
                    trip.copy(activities = trip.activities.map { if (it.id == activity.id) activity else it })
                } else trip
            }
        }
    }

    fun removeActivity(tripId: Int, activityId: Int) {
        _trips.update { trips ->
            trips.map { trip ->
                if (trip.id == tripId) {
                    Log.d(TAG, "removeActivity: removed activity id=$activityId from trip id=$tripId")
                    trip.copy(activities = trip.activities.filter { it.id != activityId })
                } else trip
            }
        }
    }

    companion object {
        private const val TAG = "FakeTripDataSource"
    }
}
