package com.example.travellikeasigma

import com.example.travellikeasigma.data.remote.HotelApiService
import com.example.travellikeasigma.data.repository.HotelRepositoryImpl
import com.example.travellikeasigma.domain.HotelRepository
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HotelRepositoryTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var repository: HotelRepository

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        repository = HotelRepositoryImpl(retrofit.create(HotelApiService::class.java))
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    // ── listHotels ──────────────────────────────────────────────────────────

    @Test
    fun `listHotels returns mapped list of ApiHotel`() = runBlocking {
        mockWebServer.enqueue(MockResponse().setBody(HOTELS_JSON).setResponseCode(200))

        val result = repository.listHotels("G13")

        assertEquals(1, result.size)
        assertEquals("BCN01", result[0].id)
        assertEquals("Hotel Ramblas", result[0].name)
        assertEquals(3, result[0].rooms.size)
        assertEquals("single", result[0].rooms[0].roomType)
        assertEquals(80.0, result[0].rooms[0].price, 0.0)
    }

    // ── checkAvailability ───────────────────────────────────────────────────

    @Test
    fun `checkAvailability returns available hotels`() = runBlocking {
        mockWebServer.enqueue(MockResponse().setBody(AVAILABILITY_JSON).setResponseCode(200))

        val result = repository.checkAvailability("G13", "2026-06-01", "2026-06-07", "london")

        assertEquals(1, result.size)
        assertEquals("LON01", result[0].id)
        assertEquals("Hotel Westminster", result[0].name)
        assertEquals(5, result[0].rating)
    }

    @Test
    fun `checkAvailability sends correct query parameters`() = runBlocking {
        mockWebServer.enqueue(MockResponse().setBody(AVAILABILITY_JSON).setResponseCode(200))

        repository.checkAvailability("G13", "2026-06-01", "2026-06-07", "london")

        val request = mockWebServer.takeRequest()
        assertTrue(request.path!!.contains("start_date=2026-06-01"))
        assertTrue(request.path!!.contains("end_date=2026-06-07"))
        assertTrue(request.path!!.contains("city=london"))
    }

    @Test
    fun `checkAvailability with server error throws exception`() = runBlocking {
        mockWebServer.enqueue(MockResponse().setResponseCode(500))

        try {
            repository.checkAvailability("G13", "2026-06-01", "2026-06-07", "london")
            assert(false) { "Expected exception was not thrown" }
        } catch (e: Exception) {
            assertNotNull(e)
        }
    }

    // ── reserve ─────────────────────────────────────────────────────────────

    @Test
    fun `reserve returns ApiReservation with correct id`() = runBlocking {
        mockWebServer.enqueue(MockResponse().setBody(RESERVE_RESPONSE_JSON).setResponseCode(200))

        val result = repository.reserve(
            groupId = "G13",
            hotelId = "LON01",
            roomId = "R1",
            startDate = "2026-06-01",
            endDate = "2026-06-07",
            guestName = "Test User",
            guestEmail = "test@test.com"
        )

        assertEquals("ABCDEF", result.id)
        assertEquals("LON01", result.hotelId)
        assertEquals("R1", result.roomId)
    }

    @Test
    fun `reserve sends correct JSON body`() = runBlocking {
        mockWebServer.enqueue(MockResponse().setBody(RESERVE_RESPONSE_JSON).setResponseCode(200))

        repository.reserve("G13", "LON01", "R1", "2026-06-01", "2026-06-07", "Test User", "test@test.com")

        val request = mockWebServer.takeRequest()
        val body = request.body.readUtf8()
        assertTrue(body.contains("\"hotel_id\":\"LON01\""))
        assertTrue(body.contains("\"room_id\":\"R1\""))
        assertTrue(body.contains("\"guest_email\":\"test@test.com\""))
    }

    // ── listReservations ────────────────────────────────────────────────────

    @Test
    fun `listReservations returns list of ApiReservation`() = runBlocking {
        mockWebServer.enqueue(MockResponse().setBody(RESERVATIONS_LIST_JSON).setResponseCode(200))

        val result = repository.listReservations("G13", null)

        assertEquals(1, result.size)
        assertEquals("ABCDEF", result[0].id)
        assertNotNull(result[0].hotel)
        assertNotNull(result[0].room)
        assertEquals("Hotel Westminster", result[0].hotel!!.name)
    }

    // ── cancelReservation ───────────────────────────────────────────────────

    @Test
    fun `cancelReservation returns true on success`() = runBlocking {
        mockWebServer.enqueue(MockResponse().setBody(CANCEL_RESPONSE_JSON).setResponseCode(200))

        val result = repository.cancelReservation("ABCDEF")

        assertTrue(result)
    }

    @Test
    fun `cancelReservation returns false on network error`() = runBlocking {
        mockWebServer.enqueue(MockResponse().setResponseCode(404))

        val result = repository.cancelReservation("XXXXXX")

        assertFalse(result)
    }

    // ── Test JSON fixtures ───────────────────────────────────────────────────

    companion object {
        private const val HOTELS_JSON = """[{"id":"BCN01","name":"Hotel Ramblas","address":"La Rambla 33, Barcelona","rating":4,"image_url":"/images/BCN01.png","rooms":[{"id":"R1","room_type":"single","price":80.0,"images":["/images/BCN01R1.png"]},{"id":"R2","room_type":"double","price":120.0,"images":["/images/BCN01R2.png"]},{"id":"R3","room_type":"suite","price":200.0,"images":["/images/BCN01R3.png"]}]}]"""

        private const val AVAILABILITY_JSON = """{"available_hotels":[{"id":"LON01","name":"Hotel Westminster","address":"Bridge St 1, London","rating":5,"image_url":"/images/LON01.png","rooms":[{"id":"R1","room_type":"single","price":80.0,"images":["/images/LON01R1.png"]},{"id":"R2","room_type":"double","price":120.0,"images":["/images/LON01R2.png"]},{"id":"R3","room_type":"suite","price":200.0,"images":["/images/LON01R3.png"]}]}]}"""

        private const val RESERVE_RESPONSE_JSON = """{"message":"Reservation confirmed","nights":6,"reservation":{"id":"ABCDEF","hotel_id":"LON01","room_id":"R1","start_date":"2026-06-01","end_date":"2026-06-07","guest_name":"Test User","guest_email":"test@test.com"}}"""

        private const val RESERVATIONS_LIST_JSON = """{"reservations":[{"id":"ABCDEF","hotel_id":"LON01","room_id":"R1","start_date":"2026-06-01","end_date":"2026-06-07","guest_name":"Test User","guest_email":"test@test.com","hotel":{"id":"LON01","name":"Hotel Westminster","address":"Bridge St 1, London","rating":5,"image_url":"/images/LON01.png"},"room":{"id":"R1","room_type":"single","price":80.0,"images":["/images/LON01R1.png"]}}]}"""

        private const val CANCEL_RESPONSE_JSON = """{"id":"ABCDEF","hotel_id":"LON01","room_id":"R1","start_date":"2026-06-01","end_date":"2026-06-07","guest_name":"Test User","guest_email":"test@test.com"}"""
    }
}
