package com.example.travellikeasigma.domain

import androidx.compose.ui.graphics.Color
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale

data class Trip(
    val id:                Int,
    val name:              String,
    val startDate:         LocalDate,
    val endDate:           LocalDate,
    val activities:        List<ItineraryActivity>,
    val places:            List<Place>,
    val photos:            List<Photo>,
    val heroColor:         Color,
    val hotel:             Hotel,
    val persons:           Int,
    val destination:       Destination
) {
    val daysCount: Int get() = ChronoUnit.DAYS.between(startDate, endDate).toInt() + 1
    val photoCount: Int get() = photos.size
    val placesCount: Int get() = places.size

    val formattedDates: String get() {
        val fmt = DateTimeFormatter.ofPattern("MMM d", Locale.ENGLISH)
        return "${startDate.format(fmt)} – ${endDate.format(fmt)}, ${endDate.year} · $daysCount days"
    }

    fun progress(): Float {
        val today = LocalDate.now()
        return when {
            today.isBefore(startDate) -> 0f
            today.isAfter(endDate)    -> 1f
            else -> {
                val total   = ChronoUnit.DAYS.between(startDate, endDate).toFloat()
                val elapsed = ChronoUnit.DAYS.between(startDate, today).toFloat()
                if (total == 0f) 1f else elapsed / total
            }
        }
    }

    fun status(): String = when {
        progress() == 0f -> "Upcoming"
        progress() >= 1f -> "Past Trip"
        else             -> "Active Trip"
    }

    fun getDateForDay(dayNumber: Int): LocalDate = startDate.plusDays((dayNumber - 1).toLong())

    fun getActivitiesByDay(dayNumber: Int): List<ItineraryActivity> {
        val date = getDateForDay(dayNumber)
        return activities.filter { it.date == date }.sortedBy { it.time }
    }

    /** Returns up to [maxDays] day numbers whose dates are today or in the future. */
    fun getUpcomingDays(maxDays: Int = 3): List<Int> {
        val today = LocalDate.now()
        return (1..daysCount)
            .filter { !getDateForDay(it).isBefore(today) }
            .take(maxDays)
    }

    fun totalCost(): Double = activities.sumOf { it.cost }
}
