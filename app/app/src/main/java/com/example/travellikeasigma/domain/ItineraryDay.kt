package com.example.travellikeasigma.model

import java.time.LocalDate

data class ItineraryDay(
    val dayNumber: Int,
    val activities: List<ItineraryActivity>
) {
    lateinit var trip: Trip

    fun getDate(): LocalDate = trip.startDate.plusDays((dayNumber - 1).toLong())
}

// ---------------------------------------------------------------------------
// Sample data — 7 days of Japan Highlights
// ---------------------------------------------------------------------------

val sampleItinerary = listOf(
    ItineraryDay(
        dayNumber = 1,
        activities = listOf(
            ItineraryActivity(1,  "10:00", "Arrive at Narita Airport", "Flight JL081 · Terminal 2", tag = ActivityType.TRANSIT),
            ItineraryActivity(2,  "12:30", "Narita Express → Shinjuku", "~90 min", 25.0, ActivityType.TRANSIT),
            ItineraryActivity(3,  "14:00", "Hotel Check-in", "Shinjuku Granbell Hotel"),
            ItineraryActivity(4,  "16:00", "Shinjuku Gyoen Garden", "National garden", 4.0, ActivityType.SIGHTSEEING),
            ItineraryActivity(5,  "19:00", "Dinner at Fuunji Ramen", "Tsukemen", 10.0, ActivityType.FOOD)
        )
    ),
    ItineraryDay(
        dayNumber = 2,
        activities = listOf(
            ItineraryActivity(6,  "09:00", "Meiji Shrine", "Harajuku", tag = ActivityType.SIGHTSEEING),
            ItineraryActivity(7,  "11:00", "Takeshita Street", "Shopping · Harajuku", tag = ActivityType.SIGHTSEEING),
            ItineraryActivity(8,  "13:00", "Lunch at Afuri Ramen", "Yuzu shio ramen", 8.0, ActivityType.FOOD),
            ItineraryActivity(9,  "15:00", "Shibuya Crossing", "Iconic scramble crossing", tag = ActivityType.SIGHTSEEING),
            ItineraryActivity(10, "19:30", "Dinner in Shibuya", "Izakaya", 28.0, ActivityType.FOOD)
        )
    ),
    ItineraryDay(
        dayNumber = 3,
        activities = listOf(
            ItineraryActivity(11, "08:00", "Shinkansen to Nikko", "~2 hrs · JR Pass", tag = ActivityType.TRANSIT),
            ItineraryActivity(12, "10:30", "Tōshō-gū Shrine", "UNESCO World Heritage", 5.0, ActivityType.SIGHTSEEING),
            ItineraryActivity(13, "12:30", "Yuba Lunch", "Local tofu-skin specialty", 12.0, ActivityType.FOOD),
            ItineraryActivity(14, "14:00", "Kegōn Falls", "97m waterfall", 5.0, ActivityType.SIGHTSEEING),
            ItineraryActivity(15, "17:00", "Return to Tokyo", "Shinkansen · ~2 hrs", tag = ActivityType.TRANSIT)
        )
    ),
    ItineraryDay(
        dayNumber = 4,
        activities = listOf(
            ItineraryActivity(16, "09:00", "Tsukiji Outer Market", "Street food · Fresh seafood", 15.0, ActivityType.FOOD),
            ItineraryActivity(17, "11:30", "teamLab Borderless", "Digital art museum", 30.0, ActivityType.SIGHTSEEING),
            ItineraryActivity(18, "14:30", "Odaiba Seaside", "Waterfront walk · Gundam statue", tag = ActivityType.SIGHTSEEING),
            ItineraryActivity(19, "18:00", "Dinner at Gonpachi", "Izakaya · Kill Bill restaurant", 35.0, ActivityType.FOOD)
        )
    ),
    ItineraryDay(
        dayNumber = 5,
        activities = listOf(
            ItineraryActivity(20, "08:30", "Romancecar to Hakone", "~85 min · Scenic route", 18.0, ActivityType.TRANSIT),
            ItineraryActivity(21, "10:30", "Hakone Open-Air Museum", "Sculpture park", 13.0, ActivityType.SIGHTSEEING),
            ItineraryActivity(22, "13:00", "Lunch at Amazake-chaya", "300-year-old teahouse", 10.0, ActivityType.FOOD),
            ItineraryActivity(23, "15:00", "Owakudani Valley", "Volcanic hot springs · Black eggs", 5.0, ActivityType.SIGHTSEEING),
            ItineraryActivity(24, "17:30", "Return to Tokyo", "Romancecar · ~85 min", 18.0, ActivityType.TRANSIT)
        )
    ),
    ItineraryDay(
        dayNumber = 6,
        activities = listOf(
            ItineraryActivity(25, "07:00", "Shinkansen to Kyoto", "~2 hrs 15 min · JR Pass", tag = ActivityType.TRANSIT),
            ItineraryActivity(26, "10:00", "Fushimi Inari Shrine", "Thousand torii gates", tag = ActivityType.SIGHTSEEING),
            ItineraryActivity(27, "12:30", "Lunch at Nishiki Market", "Kyoto's kitchen · Street food", 12.0, ActivityType.FOOD),
            ItineraryActivity(28, "14:30", "Kinkaku-ji", "Golden Pavilion", 3.0, ActivityType.SIGHTSEEING)
        )
    ),
    ItineraryDay(
        dayNumber = 7,
        activities = listOf(
            ItineraryActivity(29, "08:00", "Arashiyama Bamboo Grove", "Early morning · Less crowded", tag = ActivityType.SIGHTSEEING),
            ItineraryActivity(30, "10:30", "Tenryū-ji Temple", "UNESCO · Zen garden", 4.0, ActivityType.SIGHTSEEING),
            ItineraryActivity(31, "12:30", "Tofu Lunch at Sagano", "Yudofu set", 20.0, ActivityType.FOOD),
            ItineraryActivity(32, "15:00", "Gion District Walk", "Geisha quarter · Tea houses", tag = ActivityType.SIGHTSEEING)
        )
    )
)
