package com.example.travellikeasigma.data.room

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "itinerary_activities",
    foreignKeys = [ForeignKey(
        entity = TripEntity::class,
        parentColumns = ["id"],
        childColumns = ["tripId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("tripId")]
)
data class ActivityEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val tripId: Int,
    val time: String,
    val title: String,
    val subtitle: String,
    val cost: Double,
    val tag: String?,        // ActivityType.name or null
    val date: String         // LocalDate.toString() ISO-8601
)
