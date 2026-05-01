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
    @Query("SELECT * FROM trips WHERE userId = :userId")
    fun getAllTripsWithActivities(userId: String): Flow<List<TripWithActivities>>

    @Query("SELECT * FROM trips WHERE id = :id")
    suspend fun getTripById(id: Int): TripEntity?

    @Query("SELECT COUNT(*) FROM trips WHERE userId = :userId")
    suspend fun countTripsForUser(userId: String): Int

    @Query("SELECT COUNT(*) FROM trips")
    suspend fun countTrips(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrip(trip: TripEntity): Long

    @Query("DELETE FROM trips WHERE id = :id")
    suspend fun deleteTripById(id: Int)

    @Query("SELECT COUNT(*) FROM trips WHERE userId = :userId AND name = :name")
    suspend fun countTripsWithName(userId: String, name: String): Int
}
