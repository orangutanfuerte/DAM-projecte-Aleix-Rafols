package com.example.travellikeasigma.ui.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travellikeasigma.domain.Destination
import com.example.travellikeasigma.domain.Hotel
import com.example.travellikeasigma.domain.Trip
import com.example.travellikeasigma.domain.TripRepository
import com.example.travellikeasigma.domain.UserPreferencesRepository
import com.example.travellikeasigma.ui.theme.heroColors
import com.example.travellikeasigma.utils.TripUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class TripViewModel @Inject constructor(
    private val tripRepository: TripRepository,
    private val prefsRepository: UserPreferencesRepository
) : ViewModel() {

    companion object {
        private const val TAG = "TripViewModel"
    }

    var trips by mutableStateOf<List<Trip>>(emptyList())
        private set

    var selectedTripIndex by mutableIntStateOf(0)
        private set

    val selectedTrip: Trip?
        get() = trips.getOrNull(selectedTripIndex)

    private val userId get() = prefsRepository.getLoggedInUid() ?: ""

    // Set to true after createTrip so the Flow collector selects the new last trip
    private var pendingSelectLast = false
    private var tripsCollectionJob: Job? = null

    init {
        startCollecting()
    }

    fun reloadTrips() {
        tripsCollectionJob?.cancel()
        startCollecting()
    }

    private fun startCollecting() {
        tripsCollectionJob = viewModelScope.launch {
            tripRepository.getAllTrips(userId).collect { newTrips ->
                trips = newTrips
                selectedTripIndex = if (pendingSelectLast) {
                    pendingSelectLast = false
                    (newTrips.size - 1).coerceAtLeast(0)
                } else {
                    selectedTripIndex.coerceIn(0, (newTrips.size - 1).coerceAtLeast(0))
                }
                Log.d(TAG, "Trips updated: ${newTrips.size} trips, selectedIndex=$selectedTripIndex")
            }
        }
    }

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

        val trip = Trip(
            id = 0,  // Room assigns the real id via autoGenerate
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
        viewModelScope.launch {
            if (tripRepository.isTripNameTaken(userId, name)) {
                Log.w(TAG, "createTrip aborted: duplicate name '$name' for userId=$userId")
                return@launch
            }
            pendingSelectLast = true
            tripRepository.addTrip(trip, userId)
            Log.i(TAG, "Trip created: name='$name', dates=$startDate..$endDate")
        }
    }

    fun deleteSelectedTrip() {
        val trip = selectedTrip
        if (trip == null) {
            Log.w(TAG, "deleteSelectedTrip: no trip selected")
            return
        }
        Log.d(TAG, "Deleting trip: id=${trip.id}, name='${trip.name}'")
        viewModelScope.launch {
            tripRepository.removeTrip(trip.id)
            Log.i(TAG, "Trip deleted: id=${trip.id}")
        }
    }

    fun refreshTrips() {
        // No-op: the Flow from the repository keeps trips up to date automatically
        Log.d(TAG, "refreshTrips: handled by reactive Flow")
    }

    fun validateNewTrip(name: String, startDate: LocalDate?, endDate: LocalDate?): Boolean {
        return TripUtils.validateNewTrip(name, startDate, endDate)
    }

    suspend fun isNameAvailable(name: String): Boolean =
        !tripRepository.isTripNameTaken(userId, name)
}
