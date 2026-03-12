package com.example.travellikeasigma.ui.viewmodels

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
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class TripViewModel @Inject constructor(
    private val tripRepository: TripRepository
) : ViewModel() {

    var trips by mutableStateOf(tripRepository.getAllTrips())
        private set

    var selectedTripIndex by mutableIntStateOf(0)
        private set

    val selectedTrip: Trip?
        get() = trips.getOrNull(selectedTripIndex)

    fun selectTrip(index: Int) {
        selectedTripIndex = index.coerceIn(0, (trips.size - 1).coerceAtLeast(0))
    }

    fun createTrip(
        name: String,
        startDate: LocalDate,
        endDate: LocalDate,
        destination: Destination,
        hotel: Hotel,
        persons: Int
    ) {
        if (!validateNewTrip(name, startDate, endDate)) return

        val newId = (trips.maxOfOrNull { it.id } ?: 0) + 1
        val trip = Trip(
            id = newId,
            name = name,
            startDate = startDate,
            endDate = endDate,
            activities = emptyList(),
            packingCategories = emptyList(),
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
    }

    fun deleteSelectedTrip() {
        val trip = selectedTrip ?: return
        tripRepository.removeTrip(trip.id)
        refreshTrips()
        selectedTripIndex = selectedTripIndex.coerceIn(0, (trips.size - 1).coerceAtLeast(0))
    }

    fun refreshTrips() {
        trips = tripRepository.getAllTrips()
    }

    fun validateNewTrip(name: String, startDate: LocalDate?, endDate: LocalDate?): Boolean {
        return name.isNotBlank() && startDate != null && endDate != null && !endDate.isBefore(startDate)
    }
}
