package com.example.travellikeasigma.model

enum class ActivityType { FOOD, SIGHTSEEING, TRANSIT, OTHERS }

fun ActivityType.displayName(): String = when (this) {
    ActivityType.FOOD        -> "Food"
    ActivityType.SIGHTSEEING -> "Sightseeing"
    ActivityType.TRANSIT     -> "Transit"
    ActivityType.OTHERS      -> "Others"
}

data class ItineraryActivity(
    val id: Int,
    val time: String,
    val title: String,
    val subtitle: String,
    val cost: Double = 0.0,
    val tag: ActivityType? = null
)
