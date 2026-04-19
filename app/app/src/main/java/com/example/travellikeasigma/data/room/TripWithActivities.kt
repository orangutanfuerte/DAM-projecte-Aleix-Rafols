package com.example.travellikeasigma.data.room

import androidx.room.Embedded
import androidx.room.Relation

data class TripWithActivities(
    @Embedded val trip: TripEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "tripId"
    )
    val activities: List<ActivityEntity>
)
