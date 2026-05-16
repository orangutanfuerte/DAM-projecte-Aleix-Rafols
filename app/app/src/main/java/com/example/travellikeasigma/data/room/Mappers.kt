package com.example.travellikeasigma.data.room

import androidx.compose.ui.graphics.Color
import com.example.travellikeasigma.domain.ActivityType
import com.example.travellikeasigma.domain.ItineraryActivity
import com.example.travellikeasigma.domain.LocalReservation
import com.example.travellikeasigma.domain.Trip
import com.example.travellikeasigma.domain.sampleDestinations
import java.time.LocalDate

fun TripWithActivities.toDomain(): Trip =
    trip.toDomain(activities.map { it.toDomain() })

fun TripEntity.toDomain(activities: List<ItineraryActivity>): Trip = Trip(
    id = id,
    name = name,
    destination = sampleDestinations.find { it.id == destinationId } ?: sampleDestinations.first(),
    startDate = LocalDate.parse(startDate),
    endDate = LocalDate.parse(endDate),
    heroColor = Color(heroColor.toULong()),
    activities = activities,
    places = emptyList(),
    photos = emptyList()
)

fun Trip.toEntity(userId: String): TripEntity = TripEntity(
    id = id,
    name = name,
    destinationId = destination.id,
    startDate = startDate.toString(),
    endDate = endDate.toString(),
    heroColor = heroColor.value.toLong(),
    userId = userId
)

fun ActivityEntity.toDomain(): ItineraryActivity = ItineraryActivity(
    id = id,
    time = time,
    title = title,
    subtitle = subtitle,
    cost = cost,
    tag = tag?.let { ActivityType.valueOf(it) },
    date = LocalDate.parse(date)
)

fun ItineraryActivity.toEntity(tripId: Int): ActivityEntity = ActivityEntity(
    id = id,
    tripId = tripId,
    time = time,
    title = title,
    subtitle = subtitle,
    cost = cost,
    tag = tag?.name,
    date = date.toString()
)

fun ReservationEntity.toDomain(): LocalReservation = LocalReservation(
    id = id,
    hotelId = hotelId,
    hotelName = hotelName,
    hotelAddress = hotelAddress,
    hotelRating = hotelRating,
    hotelImageUrl = hotelImageUrl,
    roomId = roomId,
    roomType = roomType,
    pricePerNight = pricePerNight,
    roomImages = roomImages.split(",").filter { it.isNotEmpty() },
    startDate = startDate,
    endDate = endDate,
    guestName = guestName,
    guestEmail = guestEmail,
    persons = persons,
    tripName = tripName
)

fun LocalReservation.toEntity(userId: String): ReservationEntity = ReservationEntity(
    id = id,
    hotelId = hotelId,
    hotelName = hotelName,
    hotelAddress = hotelAddress,
    hotelRating = hotelRating,
    hotelImageUrl = hotelImageUrl,
    roomId = roomId,
    roomType = roomType,
    pricePerNight = pricePerNight,
    roomImages = roomImages.joinToString(","),
    startDate = startDate,
    endDate = endDate,
    guestName = guestName,
    guestEmail = guestEmail,
    persons = persons,
    userId = userId,
    tripName = tripName
)
