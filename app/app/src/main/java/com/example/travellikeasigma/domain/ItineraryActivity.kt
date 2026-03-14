package com.example.travellikeasigma.domain

import java.time.LocalDate

enum class ActivityType { FOOD, SIGHTSEEING, TRANSIT, OTHERS }

data class ItineraryActivity(
    val id: Int,
    val time: String,
    val title: String,
    val subtitle: String,
    val cost: Double = 0.0,
    val tag: ActivityType? = null,
    val date: LocalDate = LocalDate.MIN
)
