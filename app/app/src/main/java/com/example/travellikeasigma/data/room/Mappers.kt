package com.example.travellikeasigma.data.room

import androidx.compose.ui.graphics.Color
import com.example.travellikeasigma.domain.ActivityType
import com.example.travellikeasigma.domain.ItineraryActivity
import com.example.travellikeasigma.domain.Trip
import com.example.travellikeasigma.domain.sampleDestinations
import com.example.travellikeasigma.domain.sampleHotels
import java.time.LocalDate

fun TripWithActivities.toDomain(): Trip =
    trip.toDomain(activities.map { it.toDomain() })

fun TripEntity.toDomain(activities: List<ItineraryActivity>): Trip = Trip(
    id = id,
    name = name,
    destination = sampleDestinations.find { it.id == destinationId } ?: sampleDestinations.first(),
    hotel = sampleHotels.find { it.id == hotelId } ?: sampleHotels.first(),
    startDate = LocalDate.parse(startDate),
    endDate = LocalDate.parse(endDate),
    persons = persons,
    heroColor = Color(heroColor.toULong()),
    activities = activities,
    places = emptyList(),
    photos = emptyList()
)

fun Trip.toEntity(): TripEntity = TripEntity(
    id = id,
    name = name,
    destinationId = destination.id,
    hotelId = hotel.id,
    startDate = startDate.toString(),
    endDate = endDate.toString(),
    persons = persons,
    heroColor = heroColor.value.toLong()
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
