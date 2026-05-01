package com.example.travellikeasigma

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.travellikeasigma.data.room.TravelSigmaDatabase
import com.example.travellikeasigma.data.room.TripDao
import com.example.travellikeasigma.data.room.TripEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented tests for [TripDao] using an in-memory Room database.
 * Verifies SQL queries work correctly with real Room internals.
 */
@RunWith(AndroidJUnit4::class)
class TripDaoTest {

    private lateinit var database: TravelSigmaDatabase
    private lateinit var tripDao: TripDao

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            TravelSigmaDatabase::class.java
        ).allowMainThreadQueries().build()
        tripDao = database.tripDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    // ── Helper ───────────────────────────────────────────────────────────

    private fun trip(
        name: String = "Test Trip",
        userId: String = "user1"
    ) = TripEntity(
        id = 0,
        name = name,
        destinationId = 0,
        hotelId = 0,
        startDate = "2027-01-01",
        endDate = "2027-01-07",
        persons = 2,
        heroColor = 0xFF000000,
        userId = userId
    )

    // ── insert + query ───────────────────────────────────────────────────

    @Test
    fun insertTrip_canBeQueriedByUserId() = runBlocking {
        tripDao.insertTrip(trip(name = "Japan Trip", userId = "user1"))

        val result = tripDao.getAllTripsWithActivities("user1").first()

        assertEquals(1, result.size)
        assertEquals("Japan Trip", result[0].trip.name)
    }

    // ── userId filtering ─────────────────────────────────────────────────

    @Test
    fun getAllTrips_onlyReturnsTripsForGivenUser() = runBlocking {
        tripDao.insertTrip(trip(name = "User1 Trip", userId = "user1"))
        tripDao.insertTrip(trip(name = "User2 Trip", userId = "user2"))

        val result = tripDao.getAllTripsWithActivities("user1").first()

        assertEquals(1, result.size)
        assertEquals("User1 Trip", result[0].trip.name)
    }

    // ── delete ───────────────────────────────────────────────────────────

    @Test
    fun deleteTrip_removesItFromDatabase() = runBlocking {
        val id = tripDao.insertTrip(trip()).toInt()
        tripDao.deleteTripById(id)

        val result = tripDao.getAllTripsWithActivities("user1").first()

        assertTrue(result.isEmpty())
    }

    // ── count ────────────────────────────────────────────────────────────

    @Test
    fun countTripsForUser_returnsCorrectCount() = runBlocking {
        tripDao.insertTrip(trip(name = "Trip A", userId = "user1"))
        tripDao.insertTrip(trip(name = "Trip B", userId = "user1"))
        tripDao.insertTrip(trip(name = "Trip C", userId = "user2"))

        val count = tripDao.countTripsForUser("user1")

        assertEquals(2, count)
    }

    // ── duplicate name ───────────────────────────────────────────────────

    @Test
    fun countTripsWithName_detectsDuplicateForSameUser() = runBlocking {
        tripDao.insertTrip(trip(name = "Iceland Adventure", userId = "user1"))

        val count = tripDao.countTripsWithName("user1", "Iceland Adventure")

        assertEquals(1, count)
    }

    @Test
    fun countTripsWithName_doesNotConflictAcrossUsers() = runBlocking {
        tripDao.insertTrip(trip(name = "Iceland Adventure", userId = "user1"))

        // Same name but different user should NOT count as duplicate
        val count = tripDao.countTripsWithName("user2", "Iceland Adventure")

        assertEquals(0, count)
    }
}
