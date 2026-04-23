package com.example.travellikeasigma.data.repository

import android.util.Log
import androidx.compose.ui.graphics.Color
import com.example.travellikeasigma.data.room.ActivityEntity
import com.example.travellikeasigma.data.room.ItineraryActivityDao
import com.example.travellikeasigma.data.room.toDomain
import com.example.travellikeasigma.data.room.toEntity
import com.example.travellikeasigma.data.room.TripDao
import com.example.travellikeasigma.domain.ActivityType
import com.example.travellikeasigma.domain.ItineraryActivity
import com.example.travellikeasigma.domain.Trip
import com.example.travellikeasigma.domain.TripRepository
import com.example.travellikeasigma.domain.sampleDestinations
import com.example.travellikeasigma.domain.sampleHotels
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoomTripRepositoryImpl @Inject constructor(
    private val tripDao: TripDao,
    private val activityDao: ItineraryActivityDao
) : TripRepository {

    companion object {
        private const val TAG = "RoomTripRepository"
    }

    @Volatile
    private var tripsCache: List<Trip> = emptyList()

    override fun getAllTrips(userId: String): Flow<List<Trip>> =
        tripDao.getAllTripsWithActivities(userId)
            .map { rows -> rows.map { it.toDomain() } }
            .onEach { tripsCache = it }

    override fun getTripById(id: Int): Trip? = tripsCache.find { it.id == id }

    override suspend fun addTrip(trip: Trip, userId: String) {
        Log.d(TAG, "addTrip: name='${trip.name}', userId=$userId")
        tripDao.insertTrip(trip.toEntity(userId))
    }

    override suspend fun removeTrip(tripId: Int) {
        Log.d(TAG, "removeTrip: id=$tripId")
        tripDao.deleteTripById(tripId)
    }

    override suspend fun addActivity(tripId: Int, activity: ItineraryActivity) {
        Log.d(TAG, "addActivity: '${activity.title}' → tripId=$tripId")
        activityDao.insertActivity(activity.toEntity(tripId))
    }

    override suspend fun updateActivity(tripId: Int, activity: ItineraryActivity) {
        Log.d(TAG, "updateActivity: id=${activity.id}, '${activity.title}'")
        activityDao.updateActivity(activity.toEntity(tripId))
    }

    override suspend fun removeActivity(tripId: Int, activityId: Int) {
        Log.d(TAG, "removeActivity: id=$activityId from tripId=$tripId")
        activityDao.deleteActivityById(activityId)
    }

    override suspend fun seedIfEmpty(userId: String, email: String) {
        if (email != "sofia.martinez@email.com") return
        if (tripDao.countTripsForUser(userId) > 0) return
        Log.i(TAG, "Seeding initial data for userId=$userId")

        val iceland = Trip(
            id = 0,
            name = "Iceland Adventure",
            startDate = LocalDate.of(2024, 6, 1),
            endDate = LocalDate.of(2024, 6, 8),
            activities = emptyList(),
            places = emptyList(),
            photos = emptyList(),
            heroColor = Color(0xFF3A6EA5),
            destination = sampleDestinations[1],
            hotel = sampleHotels[1],
            persons = 2
        )
        val italy = Trip(
            id = 0,
            name = "Italian Getaway",
            startDate = LocalDate.of(2026, 9, 10),
            endDate = LocalDate.of(2026, 9, 18),
            activities = emptyList(),
            places = emptyList(),
            photos = emptyList(),
            heroColor = Color(0xFFC45B28),
            destination = sampleDestinations[2],
            hotel = sampleHotels[2],
            persons = 3
        )
        val japan = Trip(
            id = 0,
            name = "Japan Highlights",
            startDate = LocalDate.of(2027, 3, 14),
            endDate = LocalDate.of(2027, 3, 21),
            activities = emptyList(),
            places = emptyList(),
            photos = emptyList(),
            heroColor = Color(0xFF4A7C59),
            destination = sampleDestinations[0],
            hotel = sampleHotels[0],
            persons = 4
        )

        tripDao.insertTrip(iceland.toEntity(userId))
        tripDao.insertTrip(italy.toEntity(userId))
        val japanId = tripDao.insertTrip(japan.toEntity(userId)).toInt()

        val japanActivities = listOf(
            ActivityEntity(0, japanId, "10:00", "Arrive at Narita Airport", "Flight JL081 · Terminal 2", 0.0, ActivityType.TRANSIT.name, "2027-03-14"),
            ActivityEntity(0, japanId, "12:30", "Narita Express → Shinjuku", "~90 min", 25.0, ActivityType.TRANSIT.name, "2027-03-14"),
            ActivityEntity(0, japanId, "14:00", "Hotel Check-in", "Shinjuku Granbell Hotel", 0.0, null, "2027-03-14"),
            ActivityEntity(0, japanId, "16:00", "Shinjuku Gyoen Garden", "National garden", 4.0, ActivityType.SIGHTSEEING.name, "2027-03-14"),
            ActivityEntity(0, japanId, "19:00", "Dinner at Fuunji Ramen", "Tsukemen", 10.0, ActivityType.FOOD.name, "2027-03-14"),
            ActivityEntity(0, japanId, "09:00", "Meiji Shrine", "Harajuku", 0.0, ActivityType.SIGHTSEEING.name, "2027-03-15"),
            ActivityEntity(0, japanId, "11:00", "Takeshita Street", "Shopping · Harajuku", 0.0, ActivityType.SIGHTSEEING.name, "2027-03-15"),
            ActivityEntity(0, japanId, "13:00", "Lunch at Afuri Ramen", "Yuzu shio ramen", 8.0, ActivityType.FOOD.name, "2027-03-15"),
            ActivityEntity(0, japanId, "15:00", "Shibuya Crossing", "Iconic scramble crossing", 0.0, ActivityType.SIGHTSEEING.name, "2027-03-15"),
            ActivityEntity(0, japanId, "19:30", "Dinner in Shibuya", "Izakaya", 28.0, ActivityType.FOOD.name, "2027-03-15"),
            ActivityEntity(0, japanId, "08:00", "Shinkansen to Nikko", "~2 hrs · JR Pass", 0.0, ActivityType.TRANSIT.name, "2027-03-16"),
            ActivityEntity(0, japanId, "10:30", "Tōshō-gū Shrine", "UNESCO World Heritage", 5.0, ActivityType.SIGHTSEEING.name, "2027-03-16"),
            ActivityEntity(0, japanId, "12:30", "Yuba Lunch", "Local tofu-skin specialty", 12.0, ActivityType.FOOD.name, "2027-03-16"),
            ActivityEntity(0, japanId, "14:00", "Kegōn Falls", "97m waterfall", 5.0, ActivityType.SIGHTSEEING.name, "2027-03-16"),
            ActivityEntity(0, japanId, "17:00", "Return to Tokyo", "Shinkansen · ~2 hrs", 0.0, ActivityType.TRANSIT.name, "2027-03-16"),
            ActivityEntity(0, japanId, "09:00", "Tsukiji Outer Market", "Street food · Fresh seafood", 15.0, ActivityType.FOOD.name, "2027-03-17"),
            ActivityEntity(0, japanId, "11:30", "teamLab Borderless", "Digital art museum", 30.0, ActivityType.SIGHTSEEING.name, "2027-03-17"),
            ActivityEntity(0, japanId, "14:30", "Odaiba Seaside", "Waterfront walk · Gundam statue", 0.0, ActivityType.SIGHTSEEING.name, "2027-03-17"),
            ActivityEntity(0, japanId, "18:00", "Dinner at Gonpachi", "Izakaya · Kill Bill restaurant", 35.0, ActivityType.FOOD.name, "2027-03-17"),
            ActivityEntity(0, japanId, "08:30", "Romancecar to Hakone", "~85 min · Scenic route", 18.0, ActivityType.TRANSIT.name, "2027-03-18"),
            ActivityEntity(0, japanId, "10:30", "Hakone Open-Air Museum", "Sculpture park", 13.0, ActivityType.SIGHTSEEING.name, "2027-03-18"),
            ActivityEntity(0, japanId, "13:00", "Lunch at Amazake-chaya", "300-year-old teahouse", 10.0, ActivityType.FOOD.name, "2027-03-18"),
            ActivityEntity(0, japanId, "15:00", "Owakudani Valley", "Volcanic hot springs · Black eggs", 5.0, ActivityType.SIGHTSEEING.name, "2027-03-18"),
            ActivityEntity(0, japanId, "17:30", "Return to Tokyo", "Romancecar · ~85 min", 18.0, ActivityType.TRANSIT.name, "2027-03-18"),
            ActivityEntity(0, japanId, "07:00", "Shinkansen to Kyoto", "~2 hrs 15 min · JR Pass", 0.0, ActivityType.TRANSIT.name, "2027-03-19"),
            ActivityEntity(0, japanId, "10:00", "Fushimi Inari Shrine", "Thousand torii gates", 0.0, ActivityType.SIGHTSEEING.name, "2027-03-19"),
            ActivityEntity(0, japanId, "12:30", "Lunch at Nishiki Market", "Kyoto's kitchen · Street food", 12.0, ActivityType.FOOD.name, "2027-03-19"),
            ActivityEntity(0, japanId, "14:30", "Kinkaku-ji", "Golden Pavilion", 3.0, ActivityType.SIGHTSEEING.name, "2027-03-19"),
            ActivityEntity(0, japanId, "08:00", "Arashiyama Bamboo Grove", "Early morning · Less crowded", 0.0, ActivityType.SIGHTSEEING.name, "2027-03-20"),
            ActivityEntity(0, japanId, "10:30", "Tenryū-ji Temple", "UNESCO · Zen garden", 4.0, ActivityType.SIGHTSEEING.name, "2027-03-20"),
            ActivityEntity(0, japanId, "12:30", "Tofu Lunch at Sagano", "Yudofu set", 20.0, ActivityType.FOOD.name, "2027-03-20"),
            ActivityEntity(0, japanId, "15:00", "Gion District Walk", "Geisha quarter · Tea houses", 0.0, ActivityType.SIGHTSEEING.name, "2027-03-20")
        )
        japanActivities.forEach { activityDao.insertActivity(it) }
        Log.i(TAG, "Seeding complete: 3 trips, ${japanActivities.size} Japan activities")
    }
}
