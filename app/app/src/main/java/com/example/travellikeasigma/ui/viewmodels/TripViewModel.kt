package com.example.travellikeasigma.ui.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.travellikeasigma.domain.Destination
import com.example.travellikeasigma.domain.Hotel
import com.example.travellikeasigma.domain.Trip
import com.example.travellikeasigma.domain.TripRepository
import com.example.travellikeasigma.ui.theme.heroColors
import com.example.travellikeasigma.utils.TripUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class TripViewModel @Inject constructor(
    private val tripRepository: TripRepository
) : ViewModel() {

    companion object {
        private const val TAG = "TripViewModel"
    }

    var trips by mutableStateOf(tripRepository.getAllTrips())
        private set

    var selectedTripIndex by mutableIntStateOf(0)
        private set

    val selectedTrip: Trip?
        get() = trips.getOrNull(selectedTripIndex)

    fun selectTrip(index: Int) {
        selectedTripIndex = index.coerceIn(0, (trips.size - 1).coerceAtLeast(0))
        Log.d(TAG, "Selected trip at index $selectedTripIndex")
    }

    fun createTrip(
        name: String,
        startDate: LocalDate,
        endDate: LocalDate,
        destination: Destination,
        hotel: Hotel,
        persons: Int
    ) {
        if (!validateNewTrip(name, startDate, endDate)) {
            Log.w(TAG, "createTrip aborted: validation failed for name='$name', start=$startDate, end=$endDate")
            return
        }

        val newId = (trips.maxOfOrNull { it.id } ?: 0) + 1
        val trip = Trip(
            id = newId,
            name = name,
            startDate = startDate,
            endDate = endDate,
            activities = emptyList(),
            places = emptyList(),
            photos = emptyList(),
            heroColor = heroColors.random(),
            hotel = hotel,
            persons = persons,
            destination = destination
        )
        tripRepository.addTrip(trip)
        refreshTrips()
        selectedTripIndex = trips.size - 1
        Log.i(TAG, "Trip created: id=$newId, name='$name', dates=$startDate..$endDate")
    }

    fun deleteSelectedTrip() {
        val trip = selectedTrip
        if (trip == null) {
            Log.w(TAG, "deleteSelectedTrip: no trip selected")
            return
        }
        Log.d(TAG, "Deleting trip: id=${trip.id}, name='${trip.name}'")
        tripRepository.removeTrip(trip.id)
        refreshTrips()
        selectedTripIndex = selectedTripIndex.coerceIn(0, (trips.size - 1).coerceAtLeast(0))
        Log.i(TAG, "Trip deleted. Remaining trips: ${trips.size}")
    }

    fun refreshTrips() {
        trips = tripRepository.getAllTrips()
        Log.d(TAG, "Trips refreshed: ${trips.size} trips loaded")
    }

    // Delegates to TripUtils for reusable validation
    fun validateNewTrip(name: String, startDate: LocalDate?, endDate: LocalDate?): Boolean {
        return TripUtils.validateNewTrip(name, startDate, endDate)
    }
}
