package com.example.travellikeasigma.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.travellikeasigma.domain.ItineraryActivity
import com.example.travellikeasigma.domain.TripRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ItineraryViewModel @Inject constructor(
    private val tripRepository: TripRepository
) : ViewModel() {

    fun addActivity(tripId: Int, dayNumber: Int, activity: ItineraryActivity): Boolean {
        if (!validateActivity(activity.title, activity.time)) return false

        val trip = tripRepository.getTripById(tripId) ?: return false
        val nextId = (trip.activities.maxOfOrNull { it.id } ?: 0) + 1
        val date = trip.getDateForDay(dayNumber)
        val newActivity = activity.copy(id = nextId, date = date)
        tripRepository.addActivity(tripId, newActivity)
        return true
    }

    fun updateActivity(tripId: Int, activity: ItineraryActivity): Boolean {
        if (!validateActivity(activity.title, activity.time)) return false
        tripRepository.updateActivity(tripId, activity)
        return true
    }

    fun removeActivity(tripId: Int, activityId: Int) {
        tripRepository.removeActivity(tripId, activityId)
    }

    fun getActivity(tripId: Int, activityId: Int): ItineraryActivity? {
        return tripRepository.getTripById(tripId)?.activities?.find { it.id == activityId }
    }

    fun validateActivity(title: String, time: String): Boolean {
        return title.isNotBlank() && time.isNotBlank()
    }
}
