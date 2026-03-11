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
    val activities:    MutableList<ItineraryActivity>,
    val packingCategories: List<PackingCategory>,
    val places:        List<Place>,
    val photos:        List<Photo>,
    val heroColor:     Color, /** Unique hero-card background color for this trip */
    val hotel: Hotel,
    val persons: Int,
    val destination: Destination
) {
    val daysCount: Int get() = ChronoUnit.DAYS.between(startDate, endDate).toInt() + 1
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

    fun getDateForDay(dayNumber: Int): LocalDate = startDate.plusDays((dayNumber - 1).toLong())

    fun getActivitiesByDay(dayNumber: Int): List<ItineraryActivity> {
        val date = getDateForDay(dayNumber)
        return activities.filter { it.date == date }.sortedBy { it.time }
    }

    fun addActivity(activity: ItineraryActivity) {
        activities.add(activity)
    }

    fun removeActivity(activity: ItineraryActivity) {
        activities.removeAll { it.id == activity.id }
    }

    fun updateActivity(activity: ItineraryActivity) {
        val index = activities.indexOfFirst { it.id == activity.id }
        if (index != -1) activities[index] = activity
    }

    fun totalCost(): Double = activities.sumOf { it.cost }

    fun addImage(photo: Photo)       { /* @TODO */ }
    fun removeImage(photo: Photo)    { /* @TODO */ }
    fun addPlace(place: Place)       { /* @TODO */ }
    fun removePlace(place: Place)    { /* @TODO */ }
}

// ─────────────────────────────────────────────────────────────────────────────
// Sample data — one trip composed from other sample data
// ─────────────────────────────────────────────────────────────────────────────

private val japanActivities = mutableListOf(
    // Day 1 — Mar 14
    ItineraryActivity(1,  "10:00", "Arrive at Narita Airport", "Flight JL081 · Terminal 2", tag = ActivityType.TRANSIT, date = LocalDate.of(2027, 3, 14)),
    ItineraryActivity(2,  "12:30", "Narita Express → Shinjuku", "~90 min", 25.0, ActivityType.TRANSIT, date = LocalDate.of(2027, 3, 14)),
    ItineraryActivity(3,  "14:00", "Hotel Check-in", "Shinjuku Granbell Hotel", date = LocalDate.of(2027, 3, 14)),
    ItineraryActivity(4,  "16:00", "Shinjuku Gyoen Garden", "National garden", 4.0, ActivityType.SIGHTSEEING, date = LocalDate.of(2027, 3, 14)),
    ItineraryActivity(5,  "19:00", "Dinner at Fuunji Ramen", "Tsukemen", 10.0, ActivityType.FOOD, date = LocalDate.of(2027, 3, 14)),
    // Day 2 — Mar 15
    ItineraryActivity(6,  "09:00", "Meiji Shrine", "Harajuku", tag = ActivityType.SIGHTSEEING, date = LocalDate.of(2027, 3, 15)),
    ItineraryActivity(7,  "11:00", "Takeshita Street", "Shopping · Harajuku", tag = ActivityType.SIGHTSEEING, date = LocalDate.of(2027, 3, 15)),
    ItineraryActivity(8,  "13:00", "Lunch at Afuri Ramen", "Yuzu shio ramen", 8.0, ActivityType.FOOD, date = LocalDate.of(2027, 3, 15)),
    ItineraryActivity(9,  "15:00", "Shibuya Crossing", "Iconic scramble crossing", tag = ActivityType.SIGHTSEEING, date = LocalDate.of(2027, 3, 15)),
    ItineraryActivity(10, "19:30", "Dinner in Shibuya", "Izakaya", 28.0, ActivityType.FOOD, date = LocalDate.of(2027, 3, 15)),
    // Day 3 — Mar 16
    ItineraryActivity(11, "08:00", "Shinkansen to Nikko", "~2 hrs · JR Pass", tag = ActivityType.TRANSIT, date = LocalDate.of(2027, 3, 16)),
    ItineraryActivity(12, "10:30", "Tōshō-gū Shrine", "UNESCO World Heritage", 5.0, ActivityType.SIGHTSEEING, date = LocalDate.of(2027, 3, 16)),
    ItineraryActivity(13, "12:30", "Yuba Lunch", "Local tofu-skin specialty", 12.0, ActivityType.FOOD, date = LocalDate.of(2027, 3, 16)),
    ItineraryActivity(14, "14:00", "Kegōn Falls", "97m waterfall", 5.0, ActivityType.SIGHTSEEING, date = LocalDate.of(2027, 3, 16)),
    ItineraryActivity(15, "17:00", "Return to Tokyo", "Shinkansen · ~2 hrs", tag = ActivityType.TRANSIT, date = LocalDate.of(2027, 3, 16)),
    // Day 4 — Mar 17
    ItineraryActivity(16, "09:00", "Tsukiji Outer Market", "Street food · Fresh seafood", 15.0, ActivityType.FOOD, date = LocalDate.of(2027, 3, 17)),
    ItineraryActivity(17, "11:30", "teamLab Borderless", "Digital art museum", 30.0, ActivityType.SIGHTSEEING, date = LocalDate.of(2027, 3, 17)),
    ItineraryActivity(18, "14:30", "Odaiba Seaside", "Waterfront walk · Gundam statue", tag = ActivityType.SIGHTSEEING, date = LocalDate.of(2027, 3, 17)),
    ItineraryActivity(19, "18:00", "Dinner at Gonpachi", "Izakaya · Kill Bill restaurant", 35.0, ActivityType.FOOD, date = LocalDate.of(2027, 3, 17)),
    // Day 5 — Mar 18
    ItineraryActivity(20, "08:30", "Romancecar to Hakone", "~85 min · Scenic route", 18.0, ActivityType.TRANSIT, date = LocalDate.of(2027, 3, 18)),
    ItineraryActivity(21, "10:30", "Hakone Open-Air Museum", "Sculpture park", 13.0, ActivityType.SIGHTSEEING, date = LocalDate.of(2027, 3, 18)),
    ItineraryActivity(22, "13:00", "Lunch at Amazake-chaya", "300-year-old teahouse", 10.0, ActivityType.FOOD, date = LocalDate.of(2027, 3, 18)),
    ItineraryActivity(23, "15:00", "Owakudani Valley", "Volcanic hot springs · Black eggs", 5.0, ActivityType.SIGHTSEEING, date = LocalDate.of(2027, 3, 18)),
    ItineraryActivity(24, "17:30", "Return to Tokyo", "Romancecar · ~85 min", 18.0, ActivityType.TRANSIT, date = LocalDate.of(2027, 3, 18)),
    // Day 6 — Mar 19
    ItineraryActivity(25, "07:00", "Shinkansen to Kyoto", "~2 hrs 15 min · JR Pass", tag = ActivityType.TRANSIT, date = LocalDate.of(2027, 3, 19)),
    ItineraryActivity(26, "10:00", "Fushimi Inari Shrine", "Thousand torii gates", tag = ActivityType.SIGHTSEEING, date = LocalDate.of(2027, 3, 19)),
    ItineraryActivity(27, "12:30", "Lunch at Nishiki Market", "Kyoto's kitchen · Street food", 12.0, ActivityType.FOOD, date = LocalDate.of(2027, 3, 19)),
    ItineraryActivity(28, "14:30", "Kinkaku-ji", "Golden Pavilion", 3.0, ActivityType.SIGHTSEEING, date = LocalDate.of(2027, 3, 19)),
    // Day 7 — Mar 20
    ItineraryActivity(29, "08:00", "Arashiyama Bamboo Grove", "Early morning · Less crowded", tag = ActivityType.SIGHTSEEING, date = LocalDate.of(2027, 3, 20)),
    ItineraryActivity(30, "10:30", "Tenryū-ji Temple", "UNESCO · Zen garden", 4.0, ActivityType.SIGHTSEEING, date = LocalDate.of(2027, 3, 20)),
    ItineraryActivity(31, "12:30", "Tofu Lunch at Sagano", "Yudofu set", 20.0, ActivityType.FOOD, date = LocalDate.of(2027, 3, 20)),
    ItineraryActivity(32, "15:00", "Gion District Walk", "Geisha quarter · Tea houses", tag = ActivityType.SIGHTSEEING, date = LocalDate.of(2027, 3, 20))
)

val sampleTrips: List<Trip> = listOf(
    Trip(
        id = 1,
        name = "Iceland Adventure",
        startDate = LocalDate.of(2024, 6, 1),
        endDate = LocalDate.of(2024, 6, 8),
        activities = mutableListOf(),
        packingCategories = icelandPackingCategories,
        places = icelandPlaces,
        photos = icelandPhotos,
        heroColor = Color(0xFF3A6EA5),
        destination = sampleDestinations[1],
        hotel = sampleHotels[1],
        persons = 2
    ),

    Trip(
        id = 2,
        name = "Italian Getaway",
        startDate = LocalDate.of(2026, 9, 10),
        endDate = LocalDate.of(2026, 9, 18),
        activities = mutableListOf(),
        packingCategories = italyPackingCategories,
        places = italyPlaces,
        photos = italyPhotos,
        heroColor = Color(0xFFC45B28),
        destination = sampleDestinations[2],
        hotel = sampleHotels[2],
        persons = 3
    ),

    Trip(
        id = 3,
        name = "Japan Highlights",
        startDate = LocalDate.of(2027, 3, 14),
        endDate = LocalDate.of(2027, 3, 21),
        activities = japanActivities,
        packingCategories = samplePackingCategories,
        places = samplePlaces,
        photos = samplePhotos,
        heroColor = Color(0xFF4A7C59),
        destination = sampleDestinations[0],
        hotel = sampleHotels[0],
        persons = 4
    )
)
