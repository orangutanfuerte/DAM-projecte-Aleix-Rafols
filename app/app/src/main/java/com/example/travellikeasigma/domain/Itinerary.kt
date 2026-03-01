package com.example.travellikeasigma.model

enum class ActivityType { FOOD, SIGHTSEEING, TRANSIT, OTHERS }

fun ActivityType.displayName(): String = when (this) {
    ActivityType.FOOD -> "Food"
    ActivityType.SIGHTSEEING -> "Sightseeing"
    ActivityType.TRANSIT -> "Transit"
    ActivityType.OTHERS -> "Others"
}

enum class WeatherType { SUNNY, PARTLY_CLOUDY, CLOUDY, RAINY }

data class ItineraryActivity(
    val time: String,
    val title: String,
    val subtitle: String,
    val cost: Double = 0.0,
    val tag: ActivityType? = null
)

data class DayWeather(
    val icon: WeatherType,
    val tempHigh: Int,
    val tempLow: Int,
    val description: String
)

data class ItineraryDay(
    val dayNumber: Int,
    val date: String,
    val city: String,
    val weather: DayWeather,
    val activities: List<ItineraryActivity>
)

// ---------------------------------------------------------------------------
// Sample data — 7 days of Japan Highlights
// ---------------------------------------------------------------------------

val sampleItinerary = listOf(
    ItineraryDay(
        dayNumber = 1, date = "Mar 14, 2025", city = "Tokyo",
        weather = DayWeather(WeatherType.PARTLY_CLOUDY, 18, 12, "Partly cloudy"),
        activities = listOf(
            ItineraryActivity("10:00", "Arrive at Narita Airport", "Flight JL081 \u00b7 Terminal 2", tag = ActivityType.TRANSIT),
            ItineraryActivity("12:30", "Narita Express \u2192 Shinjuku", "~90 min", 25.0, ActivityType.TRANSIT),
            ItineraryActivity("14:00", "Hotel Check-in", "Shinjuku Granbell Hotel"),
            ItineraryActivity("16:00", "Shinjuku Gyoen Garden", "National garden", 4.0, ActivityType.SIGHTSEEING),
            ItineraryActivity("19:00", "Dinner at Fuunji Ramen", "Tsukemen", 10.0, ActivityType.FOOD)
        )
    ),
    ItineraryDay(
        dayNumber = 2, date = "Mar 15, 2025", city = "Tokyo",
        weather = DayWeather(WeatherType.SUNNY, 20, 11, "Clear skies"),
        activities = listOf(
            ItineraryActivity("09:00", "Meiji Shrine", "Harajuku", tag = ActivityType.SIGHTSEEING),
            ItineraryActivity("11:00", "Takeshita Street", "Shopping \u00b7 Harajuku", tag = ActivityType.SIGHTSEEING),
            ItineraryActivity("13:00", "Lunch at Afuri Ramen", "Yuzu shio ramen", 8.0, ActivityType.FOOD),
            ItineraryActivity("15:00", "Shibuya Crossing", "Iconic scramble crossing", tag = ActivityType.SIGHTSEEING),
            ItineraryActivity("19:30", "Dinner in Shibuya", "Izakaya", 28.0, ActivityType.FOOD)
        )
    ),
    ItineraryDay(
        dayNumber = 3, date = "Mar 16, 2025", city = "Nikko",
        weather = DayWeather(WeatherType.CLOUDY, 14, 6, "Overcast"),
        activities = listOf(
            ItineraryActivity("08:00", "Shinkansen to Nikko", "~2 hrs \u00b7 JR Pass", tag = ActivityType.TRANSIT),
            ItineraryActivity("10:30", "T\u014dsh\u014d-g\u016b Shrine", "UNESCO World Heritage", 5.0, ActivityType.SIGHTSEEING),
            ItineraryActivity("12:30", "Yuba Lunch", "Local tofu-skin specialty", 12.0, ActivityType.FOOD),
            ItineraryActivity("14:00", "Keg\u014dn Falls", "97m waterfall", 5.0, ActivityType.SIGHTSEEING),
            ItineraryActivity("17:00", "Return to Tokyo", "Shinkansen \u00b7 ~2 hrs", tag = ActivityType.TRANSIT)
        )
    ),
    ItineraryDay(
        dayNumber = 4, date = "Mar 17, 2025", city = "Tokyo",
        weather = DayWeather(WeatherType.SUNNY, 19, 10, "Sunny"),
        activities = listOf(
            ItineraryActivity("09:00", "Tsukiji Outer Market", "Street food \u00b7 Fresh seafood", 15.0, ActivityType.FOOD),
            ItineraryActivity("11:30", "teamLab Borderless", "Digital art museum", 30.0, ActivityType.SIGHTSEEING),
            ItineraryActivity("14:30", "Odaiba Seaside", "Waterfront walk \u00b7 Gundam statue", tag = ActivityType.SIGHTSEEING),
            ItineraryActivity("18:00", "Dinner at Gonpachi", "Izakaya \u00b7 Kill Bill restaurant", 35.0, ActivityType.FOOD)
        )
    ),
    ItineraryDay(
        dayNumber = 5, date = "Mar 18, 2025", city = "Hakone",
        weather = DayWeather(WeatherType.PARTLY_CLOUDY, 16, 8, "Partly cloudy"),
        activities = listOf(
            ItineraryActivity("08:30", "Romancecar to Hakone", "~85 min \u00b7 Scenic route", 18.0, ActivityType.TRANSIT),
            ItineraryActivity("10:30", "Hakone Open-Air Museum", "Sculpture park", 13.0, ActivityType.SIGHTSEEING),
            ItineraryActivity("13:00", "Lunch at Amazake-chaya", "300-year-old teahouse", 10.0, ActivityType.FOOD),
            ItineraryActivity("15:00", "Owakudani Valley", "Volcanic hot springs \u00b7 Black eggs", 5.0, ActivityType.SIGHTSEEING),
            ItineraryActivity("17:30", "Return to Tokyo", "Romancecar \u00b7 ~85 min", 18.0, ActivityType.TRANSIT)
        )
    ),
    ItineraryDay(
        dayNumber = 6, date = "Mar 19, 2025", city = "Kyoto",
        weather = DayWeather(WeatherType.RAINY, 15, 9, "Light rain"),
        activities = listOf(
            ItineraryActivity("07:00", "Shinkansen to Kyoto", "~2 hrs 15 min \u00b7 JR Pass", tag = ActivityType.TRANSIT),
            ItineraryActivity("10:00", "Fushimi Inari Shrine", "Thousand torii gates", tag = ActivityType.SIGHTSEEING),
            ItineraryActivity("12:30", "Lunch at Nishiki Market", "Kyoto\u2019s kitchen \u00b7 Street food", 12.0, ActivityType.FOOD),
            ItineraryActivity("14:30", "Kinkaku-ji", "Golden Pavilion", 3.0, ActivityType.SIGHTSEEING)
        )
    ),
    ItineraryDay(
        dayNumber = 7, date = "Mar 20, 2025", city = "Kyoto",
        weather = DayWeather(WeatherType.PARTLY_CLOUDY, 17, 10, "Clearing up"),
        activities = listOf(
            ItineraryActivity("08:00", "Arashiyama Bamboo Grove", "Early morning \u00b7 Less crowded", tag = ActivityType.SIGHTSEEING),
            ItineraryActivity("10:30", "Tenry\u016b-ji Temple", "UNESCO \u00b7 Zen garden", 4.0, ActivityType.SIGHTSEEING),
            ItineraryActivity("12:30", "Tofu Lunch at Sagano", "Yudofu set", 20.0, ActivityType.FOOD),
            ItineraryActivity("15:00", "Gion District Walk", "Geisha quarter \u00b7 Tea houses", tag = ActivityType.SIGHTSEEING)
        )
    )
)
