package com.example.travellikeasigma.domain

interface TripRepository {
    fun getAllTrips(): List<Trip>
    fun getTripById(id: Int): Trip?
    fun addTrip(trip: Trip)
    fun removeTrip(tripId: Int)
    fun addActivity(tripId: Int, activity: ItineraryActivity)
    fun updateActivity(tripId: Int, activity: ItineraryActivity)
    fun removeActivity(tripId: Int, activityId: Int)
}
