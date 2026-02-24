package com.example.travellikeasigma.model

import androidx.compose.ui.graphics.Color

/**
 * Represents a single trip.
 * In a real app this would come from a database / ViewModel.
 * For now it is a plain data class used directly in the UI.
 */
data class TripData(
    val name:          String,
    val dates:         String,
    val statusLabel:   String,   // e.g. "Past Trip", "Active Trip", "Upcoming"
    val progressFraction: Float, // 0f – 1.0f
    val progressLabel: String,   // e.g. "62%"
    val daysPlanned:   String,
    val photoCount:    String,
    val packingStatus: String,
    val placesCount:   String,
    val day1Title:     String,
    val day1Sub:       String,
    val day2Title:     String,
    val day2Sub:       String,
    val day3Title:     String,
    val day3Sub:       String,
    /** Unique hero-card background color for this trip */
    val heroColor:     Color,
    /** Whether this trip is the next upcoming one (used by ⚡ Upcoming button) */
    val isUpcoming:    Boolean = false
)

// ─────────────────────────────────────────────────────────────────────────────
// Sample data — three trips with distinct hero colors
// (orange-brown · green · blue-indigo)
// ─────────────────────────────────────────────────────────────────────────────

val sampleTrips: List<TripData> = listOf(

    TripData(
        name             = "Rome Weekend",
        dates            = "Jan 10 – Jan 13, 2024 · 3 days",
        statusLabel      = "Past Trip",
        progressFraction = 1.00f,
        progressLabel    = "100%",
        daysPlanned      = "3",
        photoCount       = "24",
        packingStatus    = "18/18",
        placesCount      = "6",
        day1Title        = "Arrival & Colosseum",
        day1Sub          = "Landed FCO · Trastevere dinner",
        day2Title        = "Vatican & Pantheon",
        day2Sub          = "Full day sightseeing",
        day3Title        = "Departure",
        day3Sub          = "Morning gelato run · fly home",
        heroColor        = Color(0xFF8D6E63),   // warm brown
        isUpcoming       = false
    ),

    TripData(
        name             = "Japan Highlights",
        dates            = "Mar 14 – Mar 28, 2025 · 14 days",
        statusLabel      = "Active Trip",
        progressFraction = 0.62f,
        progressLabel    = "62%",
        daysPlanned      = "14",
        photoCount       = "6",
        packingStatus    = "7/18",
        placesCount      = "9",
        day1Title        = "Arrival · Tokyo",
        day1Sub          = "Narita → Hotel → Shinjuku",
        day2Title        = "Shibuya & Harajuku",
        day2Sub          = "4 activities · 2 restaurants",
        day3Title        = "Day trip to Nikko",
        day3Sub          = "Depart 8:00 AM · Shinkansen",
        heroColor        = Color(0xFF4A7C59),   // forest green
        isUpcoming       = false
    ),

    TripData(
        name             = "Iceland Road Trip",
        dates            = "Aug 2 – Aug 12, 2025 · 10 days",
        statusLabel      = "Upcoming",
        progressFraction = 0.15f,
        progressLabel    = "15%",
        daysPlanned      = "10",
        photoCount       = "0",
        packingStatus    = "2/20",
        placesCount      = "4",
        day1Title        = "Fly to Reykjavik",
        day1Sub          = "Keflavík airport · Golden Circle",
        day2Title        = "South Coast",
        day2Sub          = "Seljalandsfoss · Black sand beach",
        day3Title        = "Jökulsárlón Glacier",
        day3Sub          = "Full day glacier lagoon",
        heroColor        = Color(0xFF1E6CA8),   // deep blue
        isUpcoming       = true
    )
)
