package com.example.travellikeasigma.data.fakeDB

import android.util.Log
import androidx.compose.ui.graphics.Color
import com.example.travellikeasigma.domain.ActivityType
import com.example.travellikeasigma.domain.ItineraryActivity
import com.example.travellikeasigma.domain.Trip
import com.example.travellikeasigma.domain.icelandPhotos
import com.example.travellikeasigma.domain.icelandPlaces
import com.example.travellikeasigma.domain.italyPhotos
import com.example.travellikeasigma.domain.italyPlaces
import com.example.travellikeasigma.domain.sampleDestinations
import com.example.travellikeasigma.domain.sampleHotels
import com.example.travellikeasigma.domain.samplePhotos
import com.example.travellikeasigma.domain.samplePlaces
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeTripDataSource @Inject constructor() {

    private val japanActivities = listOf(
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

    private val _trips = MutableStateFlow(
        listOf(
            Trip(
                id = 1,
                name = "Iceland Adventure",
                startDate = LocalDate.of(2024, 6, 1),
                endDate = LocalDate.of(2024, 6, 8),
                activities = emptyList(),
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
                activities = emptyList(),
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
                places = samplePlaces,
                photos = samplePhotos,
                heroColor = Color(0xFF4A7C59),
                destination = sampleDestinations[0],
                hotel = sampleHotels[0],
                persons = 4
            )
        )
    )

    fun getAllTrips(): Flow<List<Trip>> = _trips.asStateFlow()

    fun getTripById(id: Int): Trip? = _trips.value.find { it.id == id }

    fun addTrip(trip: Trip) {
        Log.d(TAG, "addTrip: id=${trip.id}, name='${trip.name}'")
        _trips.update { it + trip }
    }

    fun removeTrip(tripId: Int) {
        val exists = _trips.value.any { it.id == tripId }
        if (exists) {
            _trips.update { it.filter { t -> t.id != tripId } }
            Log.d(TAG, "removeTrip: removed trip id=$tripId")
        } else {
            Log.w(TAG, "removeTrip: no trip found with id=$tripId")
        }
    }

    fun addActivity(tripId: Int, activity: ItineraryActivity) {
        _trips.update { trips ->
            trips.map { trip ->
                if (trip.id == tripId) {
                    Log.d(TAG, "addActivity: added '${activity.title}' to trip id=$tripId")
                    trip.copy(activities = trip.activities + activity)
                } else trip
            }
        }
    }

    fun updateActivity(tripId: Int, activity: ItineraryActivity) {
        _trips.update { trips ->
            trips.map { trip ->
                if (trip.id == tripId) {
                    Log.d(TAG, "updateActivity: updated activity id=${activity.id} in trip id=$tripId")
                    trip.copy(activities = trip.activities.map { if (it.id == activity.id) activity else it })
                } else trip
            }
        }
    }

    fun removeActivity(tripId: Int, activityId: Int) {
        _trips.update { trips ->
            trips.map { trip ->
                if (trip.id == tripId) {
                    Log.d(TAG, "removeActivity: removed activity id=$activityId from trip id=$tripId")
                    trip.copy(activities = trip.activities.filter { it.id != activityId })
                } else trip
            }
        }
    }

    companion object {
        private const val TAG = "FakeTripDataSource"
    }
}
