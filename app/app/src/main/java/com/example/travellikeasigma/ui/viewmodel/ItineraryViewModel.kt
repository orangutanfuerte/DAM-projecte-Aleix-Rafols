package com.example.travellikeasigma.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.travellikeasigma.model.ItineraryActivity
import com.example.travellikeasigma.model.ItineraryDay
import com.example.travellikeasigma.model.sampleItinerary
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ItineraryViewModel : ViewModel() {

    private val _days = MutableStateFlow(sampleItinerary)
    val days: StateFlow<List<ItineraryDay>> = _days.asStateFlow()

    private val _selectedDayIndex = MutableStateFlow(0)
    val selectedDayIndex: StateFlow<Int> = _selectedDayIndex.asStateFlow()

    fun selectDay(index: Int) {
        _selectedDayIndex.value = index
    }

    fun addActivity(dayNumber: Int, activity: ItineraryActivity) {
        _days.value = _days.value.map { day ->
            if (day.dayNumber == dayNumber) {
                day.copy(
                    activities = (day.activities + activity).sortedBy { it.time }
                )
            } else {
                day
            }
        }
    }
}
