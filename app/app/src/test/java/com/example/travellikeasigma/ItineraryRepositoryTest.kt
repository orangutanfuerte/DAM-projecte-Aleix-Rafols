package com.example.travellikeasigma

import androidx.compose.ui.graphics.Color
import com.example.travellikeasigma.data.fakeDB.FakeTripDataSource
import com.example.travellikeasigma.data.repository.TripRepositoryImpl
import com.example.travellikeasigma.domain.ActivityType
import com.example.travellikeasigma.domain.Destination
import com.example.travellikeasigma.domain.Hotel
import com.example.travellikeasigma.domain.ItineraryActivity
import com.example.travellikeasigma.domain.Trip
import com.example.travellikeasigma.domain.TripRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.time.LocalDate

/**
 * Unit tests for itinerary activity CRUD operations through [TripRepositoryImpl].
 * Tests adding, updating, and removing activities within a trip.
 */
class ItineraryRepositoryTest {

    private lateinit var dataSource: FakeTripDataSource
    private lateinit var repository: TripRepository

    // Test trip that we'll add activities to (starts empty)
    private val testDestination = Destination(99, "Test Country")
    private val testHotel = Hotel(99, "Test Hotel", 100.0, testDestination)
    private lateinit var testTrip: Trip

    @Before
    fun setUp() {
        dataSource = FakeTripDataSource()
        repository = TripRepositoryImpl(dataSource)

        // Add a clean trip with no activities for isolated testing
        testTrip = Trip(
            id = 100,
            name = "Activity Test Trip",
            startDate = LocalDate.of(2027, 6, 1),
            endDate = LocalDate.of(2027, 6, 7),
            activities = emptyList(),
            places = emptyList(),
            photos = emptyList(),
            heroColor = Color(0xFFFF0000),
            hotel = testHotel,
            persons = 2,
            destination = testDestination
        )
        runBlocking { repository.addTrip(testTrip, "") }
    }

    // ── Helper ───────────────────────────────────────────────────────────

    private fun createTestActivity(
        id: Int = 1,
        time: String = "10:00",
        title: String = "Test Activity",
        subtitle: String = "Test subtitle",
        cost: Double = 15.0,
        tag: ActivityType? = ActivityType.SIGHTSEEING,
        date: LocalDate = LocalDate.of(2027, 6, 1)
    ): ItineraryActivity = ItineraryActivity(id, time, title, subtitle, cost, tag, date)

    // ── addActivity ──────────────────────────────────────────────────────

    @Test
    fun `addActivity increases activity count by 1`() {
        val activity = createTestActivity(id = 1, title = "Visit Museum")
        runBlocking { repository.addActivity(testTrip.id, activity) }

        val trip = repository.getTripById(testTrip.id)!!
        assertEquals(1, trip.activities.size)
    }

    @Test
    fun `addActivity preserves all activity fields`() {
        val activity = createTestActivity(
            id = 1,
            time = "14:30",
            title = "Temple Visit",
            subtitle = "Ancient temple",
            cost = 5.0,
            tag = ActivityType.SIGHTSEEING,
            date = LocalDate.of(2027, 6, 1)
        )
        runBlocking { repository.addActivity(testTrip.id, activity) }

        val saved = repository.getTripById(testTrip.id)!!.activities.first()
        assertEquals(1, saved.id)
        assertEquals("14:30", saved.time)
        assertEquals("Temple Visit", saved.title)
        assertEquals("Ancient temple", saved.subtitle)
        assertEquals(5.0, saved.cost, 0.001)
        assertEquals(ActivityType.SIGHTSEEING, saved.tag)
        assertEquals(LocalDate.of(2027, 6, 1), saved.date)
    }

    @Test
    fun `addActivity can add multiple activities to the same trip`() {
        runBlocking {
            repository.addActivity(testTrip.id, createTestActivity(id = 1, title = "Breakfast"))
            repository.addActivity(testTrip.id, createTestActivity(id = 2, title = "Lunch"))
            repository.addActivity(testTrip.id, createTestActivity(id = 3, title = "Dinner"))
        }

        val trip = repository.getTripById(testTrip.id)!!
        assertEquals(3, trip.activities.size)
    }

    @Test
    fun `addActivity with non-existing tripId does nothing`() {
        val activity = createTestActivity(id = 1, title = "Ghost Activity")
        runBlocking { repository.addActivity(9999, activity) } // Trip doesn't exist

        // The activity should not appear anywhere
        assertNull(repository.getTripById(9999))
    }

    @Test
    fun `addActivity with zero cost is valid`() {
        val activity = createTestActivity(id = 1, cost = 0.0, title = "Free Entry")
        runBlocking { repository.addActivity(testTrip.id, activity) }

        val saved = repository.getTripById(testTrip.id)!!.activities.first()
        assertEquals(0.0, saved.cost, 0.001)
    }

    @Test
    fun `addActivity with null tag is valid`() {
        val activity = createTestActivity(id = 1, tag = null, title = "Untagged")
        runBlocking { repository.addActivity(testTrip.id, activity) }

        val saved = repository.getTripById(testTrip.id)!!.activities.first()
        assertNull(saved.tag)
    }

    // ── updateActivity ───────────────────────────────────────────────────

    @Test
    fun `updateActivity changes the title of an existing activity`() {
        val original = createTestActivity(id = 1, title = "Original Title")
        runBlocking { repository.addActivity(testTrip.id, original) }

        val updated = original.copy(title = "Updated Title")
        runBlocking { repository.updateActivity(testTrip.id, updated) }

        val result = repository.getTripById(testTrip.id)!!.activities.first()
        assertEquals("Updated Title", result.title)
    }

    @Test
    fun `updateActivity changes the cost of an existing activity`() {
        val original = createTestActivity(id = 1, cost = 10.0)
        runBlocking { repository.addActivity(testTrip.id, original) }

        val updated = original.copy(cost = 25.0)
        runBlocking { repository.updateActivity(testTrip.id, updated) }

        val result = repository.getTripById(testTrip.id)!!.activities.first()
        assertEquals(25.0, result.cost, 0.001)
    }

    @Test
    fun `updateActivity changes the time of an existing activity`() {
        val original = createTestActivity(id = 1, time = "10:00")
        runBlocking { repository.addActivity(testTrip.id, original) }

        val updated = original.copy(time = "15:30")
        runBlocking { repository.updateActivity(testTrip.id, updated) }

        val result = repository.getTripById(testTrip.id)!!.activities.first()
        assertEquals("15:30", result.time)
    }

    @Test
    fun `updateActivity changes the tag of an existing activity`() {
        val original = createTestActivity(id = 1, tag = ActivityType.FOOD)
        runBlocking { repository.addActivity(testTrip.id, original) }

        val updated = original.copy(tag = ActivityType.TRANSIT)
        runBlocking { repository.updateActivity(testTrip.id, updated) }

        val result = repository.getTripById(testTrip.id)!!.activities.first()
        assertEquals(ActivityType.TRANSIT, result.tag)
    }

    @Test
    fun `updateActivity does not affect other activities in the same trip`() {
        val activity1 = createTestActivity(id = 1, title = "Keep This")
        val activity2 = createTestActivity(id = 2, title = "Change This")
        runBlocking {
            repository.addActivity(testTrip.id, activity1)
            repository.addActivity(testTrip.id, activity2)
        }

        val updated2 = activity2.copy(title = "Changed")
        runBlocking { repository.updateActivity(testTrip.id, updated2) }

        val activities = repository.getTripById(testTrip.id)!!.activities
        assertEquals("Keep This", activities.find { it.id == 1 }!!.title)
        assertEquals("Changed", activities.find { it.id == 2 }!!.title)
    }

    @Test
    fun `updateActivity with non-existing tripId does not crash`() {
        val activity = createTestActivity(id = 1, title = "Ghost")
        // Should not throw any exception
        runBlocking { repository.updateActivity(9999, activity) }
    }

    // ── removeActivity ───────────────────────────────────────────────────

    @Test
    fun `removeActivity decreases activity count by 1`() {
        runBlocking {
            repository.addActivity(testTrip.id, createTestActivity(id = 1, title = "To Remove"))
            repository.addActivity(testTrip.id, createTestActivity(id = 2, title = "To Keep"))
        }
        assertEquals(2, repository.getTripById(testTrip.id)!!.activities.size)

        runBlocking { repository.removeActivity(testTrip.id, 1) }
        assertEquals(1, repository.getTripById(testTrip.id)!!.activities.size)
    }

    @Test
    fun `removeActivity removes the correct activity`() {
        runBlocking {
            repository.addActivity(testTrip.id, createTestActivity(id = 1, title = "Remove Me"))
            repository.addActivity(testTrip.id, createTestActivity(id = 2, title = "Keep Me"))
            repository.removeActivity(testTrip.id, 1)
        }

        val remaining = repository.getTripById(testTrip.id)!!.activities
        assertEquals(1, remaining.size)
        assertEquals("Keep Me", remaining.first().title)
    }

    @Test
    fun `removeActivity with non-existing activityId does not change the list`() {
        runBlocking { repository.addActivity(testTrip.id, createTestActivity(id = 1, title = "Stays")) }

        runBlocking { repository.removeActivity(testTrip.id, 999) } // Non-existing activity

        val activities = repository.getTripById(testTrip.id)!!.activities
        assertEquals(1, activities.size)
    }

    @Test
    fun `removeActivity with non-existing tripId does not crash`() {
        // Should not throw any exception
        runBlocking { repository.removeActivity(9999, 1) }
    }

    // ── Simulated user flow: full activity lifecycle ─────────────────────

    @Test
    fun `full lifecycle - add, update, and remove an activity`() {
        // 1. Add activity
        val activity = createTestActivity(id = 1, title = "Morning Run", time = "07:00", cost = 0.0)
        runBlocking { repository.addActivity(testTrip.id, activity) }
        assertEquals(1, repository.getTripById(testTrip.id)!!.activities.size)

        // 2. Update it
        val updated = activity.copy(title = "Morning Yoga", time = "06:30")
        runBlocking { repository.updateActivity(testTrip.id, updated) }
        val afterUpdate = repository.getTripById(testTrip.id)!!.activities.first()
        assertEquals("Morning Yoga", afterUpdate.title)
        assertEquals("06:30", afterUpdate.time)

        // 3. Remove it
        runBlocking { repository.removeActivity(testTrip.id, 1) }
        assertTrue(repository.getTripById(testTrip.id)!!.activities.isEmpty())
    }

    @Test
    fun `totalCost sums all activities correctly`() {
        runBlocking {
            repository.addActivity(testTrip.id, createTestActivity(id = 1, cost = 10.0))
            repository.addActivity(testTrip.id, createTestActivity(id = 2, cost = 25.50))
            repository.addActivity(testTrip.id, createTestActivity(id = 3, cost = 4.50))
        }

        val trip = repository.getTripById(testTrip.id)!!
        assertEquals(40.0, trip.totalCost(), 0.001)
    }

    // ── Japan trip sample data integrity ─────────────────────────────────

    @Test
    fun `preloaded Japan trip has 32 activities`() {
        val japan = repository.getTripById(3)
        assertNotNull(japan)
        assertEquals(32, japan!!.activities.size)
    }

    @Test
    fun `getActivitiesByDay filters activities correctly for Japan day 1`() {
        val japan = repository.getTripById(3)!!
        // Day 1 = Mar 14, should have 5 activities
        val day1Activities = japan.getActivitiesByDay(1)
        assertEquals(5, day1Activities.size)
    }
}
