package com.example.travellikeasigma.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface TripDao {

    @Transaction
    @Query("SELECT * FROM trips")
    fun getAllTripsWithActivities(): Flow<List<TripWithActivities>>

    @Query("SELECT * FROM trips WHERE id = :id")
    suspend fun getTripById(id: Int): TripEntity?

    @Query("SELECT COUNT(*) FROM trips")
    suspend fun countTrips(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrip(trip: TripEntity): Long

    @Query("DELETE FROM trips WHERE id = :id")
    suspend fun deleteTripById(id: Int)
}
