package com.example.travellikeasigma.model

import androidx.compose.ui.graphics.Color
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale

/**
 * Represents a single trip.
 * In a real app this would come from a database / ViewModel.
 * For now it is a plain data class used directly in the UI.
 */
data class Trip(
    val id:            Int,
    val name:          String,
    val startDate:     LocalDate,
    val endDate:       LocalDate,
    val itinerary:     List<ItineraryDay>,
    val packingCategories: List<PackingCategory>,
    val places:        List<Place>,
    val photos:        List<Photo>,
    val heroColor:     Color, /** Unique hero-card background color for this trip */
    val hotel: Hotel,
    val persons: Int,
    val destination: Destination
) {
    val daysCount: Int get() = itinerary.size
    val photoCount: Int get() = photos.size
    val placesCount: Int get() = places.size
    val totalPackingItems: Int get() = packingCategories.flatMap { it.items }.size
    val packedPackingItems: Int get() = packingCategories.flatMap { it.items }.count { it.isPacked }

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

    fun addImage(photo: Photo)       { /* @TODO */ }
    fun removeImage(photo: Photo)    { /* @TODO */ }
    fun addPlace(place: Place)       { /* @TODO */ }
    fun removePlace(place: Place)    { /* @TODO */ }
    fun getUpcomingDays(): List<ItineraryDay> { return emptyList() /* @TODO */ }

    fun addActivity(dayNumber: Int, activity: ItineraryActivity)    { /* @TODO */ }
    fun removeActivity(dayNumber: Int, activity: ItineraryActivity) { /* @TODO */ }
    fun totalCost(): Double = itinerary.flatMap { it.activities }.sumOf { it.cost }
}

// ─────────────────────────────────────────────────────────────────────────────
// Sample data — one trip composed from other sample data
// ─────────────────────────────────────────────────────────────────────────────

val sampleTrips: List<Trip> = listOf(
    Trip(
        id = 1,
        name = "Iceland Adventure",
        startDate = LocalDate.of(2024, 6, 1),
        endDate = LocalDate.of(2024, 6, 8),
        itinerary = icelandItinerary,
        packingCategories = icelandPackingCategories,
        places = icelandPlaces,
        photos = icelandPhotos,
        heroColor = Color(0xFF3A6EA5),
        destination = sampleDestinations[1],
        hotel = sampleHotels[1],
        persons = 2
    ).also { trip -> trip.itinerary.forEach { it.trip = trip } },

    Trip(
        id = 2,
        name = "Italian Getaway",
        startDate = LocalDate.of(2026, 9, 10),
        endDate = LocalDate.of(2026, 9, 18),
        itinerary = italyItinerary,
        packingCategories = italyPackingCategories,
        places = italyPlaces,
        photos = italyPhotos,
        heroColor = Color(0xFFC45B28),
        destination = sampleDestinations[2],
        hotel = sampleHotels[2],
        persons = 3
    ).also { trip -> trip.itinerary.forEach { it.trip = trip } },

    Trip(
        id = 3,
        name = "Japan Highlights",
        startDate = LocalDate.of(2027, 3, 14),
        endDate = LocalDate.of(2027, 3, 21),
        itinerary = sampleItinerary,
        packingCategories = samplePackingCategories,
        places = samplePlaces,
        photos = samplePhotos,
        heroColor = Color(0xFF4A7C59),
        destination = sampleDestinations[0],
        hotel = sampleHotels[0],
        persons = 4
    ).also { trip -> trip.itinerary.forEach { it.trip = trip } }
)
