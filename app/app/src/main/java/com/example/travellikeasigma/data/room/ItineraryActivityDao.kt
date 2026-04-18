package com.example.travellikeasigma.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface ItineraryActivityDao {

    @Query("SELECT * FROM itinerary_activities WHERE tripId = :tripId ORDER BY date, time")
    suspend fun getActivitiesForTrip(tripId: Int): List<ActivityEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActivity(activity: ActivityEntity): Long

    @Update
    suspend fun updateActivity(activity: ActivityEntity)

    @Query("DELETE FROM itinerary_activities WHERE id = :id")
    suspend fun deleteActivityById(id: Int)
}
