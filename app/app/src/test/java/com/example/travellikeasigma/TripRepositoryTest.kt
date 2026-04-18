package com.example.travellikeasigma

import androidx.compose.ui.graphics.Color
import com.example.travellikeasigma.data.fakeDB.FakeTripDataSource
import com.example.travellikeasigma.data.repository.TripRepositoryImpl
import com.example.travellikeasigma.domain.Destination
import com.example.travellikeasigma.domain.Hotel
import com.example.travellikeasigma.domain.Trip
import com.example.travellikeasigma.domain.TripRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.time.LocalDate

/**
 * Unit tests for trip CRUD operations through [TripRepositoryImpl].
 * Uses [FakeTripDataSource] as the in-memory data source (no mocking needed).
 */
class TripRepositoryTest {

    private lateinit var dataSource: FakeTripDataSource
    private lateinit var repository: TripRepository

    // Reusable test data
    private val testDestination = Destination(99, "Test Country")
    private val testHotel = Hotel(99, "Test Hotel", 100.0, testDestination)

    @Before
    fun setUp() {
        // FakeTripDataSource loads 3 sample trips on construction (ids: 1, 2, 3)
        dataSource = FakeTripDataSource()
        repository = TripRepositoryImpl(dataSource)
    }

    // ── Helper ───────────────────────────────────────────────────────────

    private fun createTestTrip(
        id: Int = 100,
        name: String = "Test Trip",
        startDate: LocalDate = LocalDate.of(2027, 1, 1),
        endDate: LocalDate = LocalDate.of(2027, 1, 7)
    ): Trip = Trip(
        id = id,
        name = name,
        startDate = startDate,
        endDate = endDate,
        activities = emptyList(),
        places = emptyList(),
        photos = emptyList(),
        heroColor = Color(0xFF000000),
        hotel = testHotel,
        persons = 2,
        destination = testDestination
    )

    // Convenience: get the current list from the Flow
    private fun allTrips() = runBlocking { repository.getAllTrips().first() }

    // ── getAllTrips ───────────────────────────────────────────────────────

    @Test
    fun `getAllTrips returns the 3 preloaded sample trips`() {
        assertEquals(3, allTrips().size)
    }

    @Test
    fun `getAllTrips returns a new list each time (defensive copy)`() {
        val list1 = allTrips()
        val list2 = allTrips()
        assertEquals(list1, list2)
    }

    // ── getTripById ──────────────────────────────────────────────────────

    @Test
    fun `getTripById returns correct trip for existing id`() {
        val trip = repository.getTripById(1)
        assertNotNull(trip)
        assertEquals("Iceland Adventure", trip!!.name)
    }

    @Test
    fun `getTripById returns null for non-existing id`() {
        val trip = repository.getTripById(999)
        assertNull(trip)
    }

    // ── addTrip ──────────────────────────────────────────────────────────

    @Test
    fun `addTrip increases trip count by 1`() {
        val initialCount = allTrips().size
        runBlocking { repository.addTrip(createTestTrip()) }
        assertEquals(initialCount + 1, allTrips().size)
    }

    @Test
    fun `addTrip makes the new trip retrievable by id`() {
        val trip = createTestTrip(id = 42, name = "My New Trip")
        runBlocking { repository.addTrip(trip) }

        val retrieved = repository.getTripById(42)
        assertNotNull(retrieved)
        assertEquals("My New Trip", retrieved!!.name)
    }

    @Test
    fun `addTrip preserves all trip fields correctly`() {
        val start = LocalDate.of(2027, 5, 1)
        val end = LocalDate.of(2027, 5, 15)
        val trip = createTestTrip(id = 50, name = "Full Check", startDate = start, endDate = end)
        runBlocking { repository.addTrip(trip) }

        val retrieved = repository.getTripById(50)!!
        assertEquals(50, retrieved.id)
        assertEquals("Full Check", retrieved.name)
        assertEquals(start, retrieved.startDate)
        assertEquals(end, retrieved.endDate)
        assertEquals(2, retrieved.persons)
        assertEquals("Test Hotel", retrieved.hotel.name)
        assertEquals("Test Country", retrieved.destination.destinationName)
        assertTrue(retrieved.activities.isEmpty())
    }

    // ── removeTrip ───────────────────────────────────────────────────────

    @Test
    fun `removeTrip decreases trip count by 1`() {
        val initialCount = allTrips().size
        runBlocking { repository.removeTrip(1) } // Remove "Iceland Adventure"
        assertEquals(initialCount - 1, allTrips().size)
    }

    @Test
    fun `removeTrip makes the trip no longer retrievable`() {
        runBlocking { repository.removeTrip(1) }
        assertNull(repository.getTripById(1))
    }

    @Test
    fun `removeTrip with non-existing id does not change trip count`() {
        val initialCount = allTrips().size
        runBlocking { repository.removeTrip(999) }
        assertEquals(initialCount, allTrips().size)
    }

    @Test
    fun `removeTrip does not affect other trips`() {
        runBlocking { repository.removeTrip(1) } // Remove Iceland
        assertNotNull(repository.getTripById(2)) // Italy still exists
        assertNotNull(repository.getTripById(3)) // Japan still exists
    }

    // ── Simulate user flow: create then delete ───────────────────────────

    @Test
    fun `create a trip then remove it returns to initial state`() {
        val initialSize = allTrips().size

        val trip = createTestTrip(id = 77, name = "Temporary Trip")
        runBlocking { repository.addTrip(trip) }
        assertEquals(initialSize + 1, allTrips().size)

        runBlocking { repository.removeTrip(77) }
        assertEquals(initialSize, allTrips().size)
        assertNull(repository.getTripById(77))
    }

    // ── Trip data class computed properties ──────────────────────────────

    @Test
    fun `daysCount is calculated correctly for a 7-day trip`() {
        val trip = createTestTrip(
            startDate = LocalDate.of(2027, 1, 1),
            endDate = LocalDate.of(2027, 1, 7)
        )
        assertEquals(7, trip.daysCount)
    }

    @Test
    fun `daysCount is 1 when startDate equals endDate`() {
        val date = LocalDate.of(2027, 3, 15)
        val trip = createTestTrip(startDate = date, endDate = date)
        assertEquals(1, trip.daysCount)
    }

    @Test
    fun `totalCost returns 0 for a trip with no activities`() {
        val trip = createTestTrip()
        assertEquals(0.0, trip.totalCost(), 0.001)
    }

    @Test
    fun `getDateForDay returns correct date`() {
        val trip = createTestTrip(
            startDate = LocalDate.of(2027, 1, 1),
            endDate = LocalDate.of(2027, 1, 7)
        )
        // Day 1 = startDate
        assertEquals(LocalDate.of(2027, 1, 1), trip.getDateForDay(1))
        // Day 3 = startDate + 2
        assertEquals(LocalDate.of(2027, 1, 3), trip.getDateForDay(3))
    }
}
