package com.example.travellikeasigma.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.travellikeasigma.domain.ItineraryActivity
import com.example.travellikeasigma.domain.TripRepository
import com.example.travellikeasigma.utils.ItineraryUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ItineraryViewModel @Inject constructor(
    private val tripRepository: TripRepository
) : ViewModel() {

    companion object {
        private const val TAG = "ItineraryViewModel"
    }

    fun addActivity(tripId: Int, dayNumber: Int, activity: ItineraryActivity): Boolean {
        if (!validateActivity(activity.title, activity.time)) {
            Log.w(TAG, "addActivity aborted: validation failed (title='${activity.title}', time='${activity.time}')")
            return false
        }

        val trip = tripRepository.getTripById(tripId)
        if (trip == null) {
            Log.e(TAG, "addActivity failed: trip with id=$tripId not found")
            return false
        }

        // Validate that dayNumber is within the trip's date range
        if (!ItineraryUtils.isValidDayNumber(dayNumber, trip.daysCount)) {
            Log.w(TAG, "addActivity aborted: day $dayNumber is out of range for trip (1..${trip.daysCount})")
            return false
        }

        val nextId = (trip.activities.maxOfOrNull { it.id } ?: 0) + 1
        val date = trip.getDateForDay(dayNumber)
        val newActivity = activity.copy(id = nextId, date = date)
        tripRepository.addActivity(tripId, newActivity)
        Log.i(TAG, "Activity added: id=$nextId, title='${activity.title}', tripId=$tripId, day=$dayNumber")
        return true
    }

    fun updateActivity(tripId: Int, activity: ItineraryActivity): Boolean {
        if (!validateActivity(activity.title, activity.time)) {
            Log.w(TAG, "updateActivity aborted: validation failed (title='${activity.title}', time='${activity.time}')")
            return false
        }
        tripRepository.updateActivity(tripId, activity)
        Log.i(TAG, "Activity updated: id=${activity.id}, title='${activity.title}', tripId=$tripId")
        return true
    }

    fun removeActivity(tripId: Int, activityId: Int) {
        Log.d(TAG, "Removing activity: id=$activityId from tripId=$tripId")
        tripRepository.removeActivity(tripId, activityId)
        Log.i(TAG, "Activity removed: id=$activityId, tripId=$tripId")
    }

    fun getActivity(tripId: Int, activityId: Int): ItineraryActivity? {
        val activity = tripRepository.getTripById(tripId)?.activities?.find { it.id == activityId }
        if (activity == null) {
            Log.w(TAG, "getActivity: activity id=$activityId not found in tripId=$tripId")
        }
        return activity
    }

    // Delegates to ItineraryUtils for reusable validation
    fun validateActivity(title: String, time: String): Boolean {
        return ItineraryUtils.validateActivity(title, time)
    }
}
