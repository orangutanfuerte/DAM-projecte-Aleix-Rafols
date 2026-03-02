package com.example.travellikeasigma.model

import androidx.compose.ui.graphics.Color

data class Photo(val color: Color)

/**
 * Represents a single trip.
 * In a real app this would come from a database / ViewModel.
 * For now it is a plain data class used directly in the UI.
 */
data class Trip(
    val name:          String,
    val dates:         String,
    val statusLabel:   String,   // e.g. "Past Trip", "Active Trip", "Upcoming"
    val progressFraction: Float, // 0f – 1.0f
    val progressLabel: String,   // e.g. "62%"
    val itinerary:     List<ItineraryDay>,
    val packingCategories: List<PackingCategory>,
    val places:        List<Place>,
    val photos:        List<Photo>,
    /** Unique hero-card background color for this trip */
    val heroColor:     Color,
    /** Whether this trip is the next upcoming one (used by Upcoming button) */
    val isUpcoming:    Boolean = false
) {
    val daysCount: Int get() = itinerary.size
    val photoCount: Int get() = photos.size
    val placesCount: Int get() = places.size
    val totalPackingItems: Int get() = packingCategories.flatMap { it.items }.size
    val packedPackingItems: Int get() = packingCategories.flatMap { it.items }.count { it.isPacked }
}

// ─────────────────────────────────────────────────────────────────────────────
// New-trip creation models
// ─────────────────────────────────────────────────────────────────────────────

data class Hotel(
    val id: Int,
    val name: String,
    val pricePerNight: Double
)

data class NewTripInput(
    val destination: String,
    val tripName: String,
    val persons: Int,
    val checkInMillis: Long,
    val checkOutMillis: Long,
    val hotel: Hotel
)

val sampleCountries = listOf("Japan", "Iceland", "Italy", "Spain", "Brasil")

val sampleHotels = listOf(
    Hotel(1, "Papa Hotel",   89.0),
    Hotel(2, "Pepe Hotel",  124.0),
    Hotel(3, "Pipi Hotel",   67.0),
    Hotel(4, "Popo Hotel",  210.0),
    Hotel(5, "Pupu Hotel",  155.0)
)

// ─────────────────────────────────────────────────────────────────────────────
// Sample photos (moved from PhotosScreen)
// ─────────────────────────────────────────────────────────────────────────────

val samplePhotos = listOf(
    Photo(Color(0xFFB2E2F0)),
    Photo(Color(0xFFA37AC2)),
    Photo(Color(0xFFFFB346)),
    Photo(Color(0xFFCCF4FA)),
    Photo(Color(0xFF7851A8)),
    Photo(Color(0xFFFFF09C)),
)

// ─────────────────────────────────────────────────────────────────────────────
// Sample data — one trip composed from other sample data
// ─────────────────────────────────────────────────────────────────────────────

val sampleTrips: List<Trip> = listOf(
    Trip(
        name             = "Japan Highlights",
        dates            = "Mar 14 – Mar 21, 2025 · 7 days",
        statusLabel      = "Active Trip",
        progressFraction = 0.62f,
        progressLabel    = "62%",
        itinerary        = sampleItinerary,
        packingCategories = samplePackingCategories,
        places           = samplePlaces,
        photos           = samplePhotos,
        heroColor        = Color(0xFF4A7C59),   // forest green
        isUpcoming       = false
    )
)
