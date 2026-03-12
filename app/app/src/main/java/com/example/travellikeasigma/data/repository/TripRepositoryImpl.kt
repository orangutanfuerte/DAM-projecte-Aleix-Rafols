package com.example.travellikeasigma.data.repository

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

    override fun getAllTrips(): List<Trip> = dataSource.getAllTrips()

    override fun getTripById(id: Int): Trip? = dataSource.getTripById(id)

    override fun addTrip(trip: Trip) = dataSource.addTrip(trip)

    override fun removeTrip(tripId: Int) = dataSource.removeTrip(tripId)

    override fun addActivity(tripId: Int, activity: ItineraryActivity) =
        dataSource.addActivity(tripId, activity)

    override fun updateActivity(tripId: Int, activity: ItineraryActivity) =
        dataSource.updateActivity(tripId, activity)

    override fun removeActivity(tripId: Int, activityId: Int) =
        dataSource.removeActivity(tripId, activityId)
}
