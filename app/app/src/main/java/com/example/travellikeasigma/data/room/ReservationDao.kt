package com.example.travellikeasigma.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ReservationDao {
    @Query("SELECT * FROM reservations WHERE userId = :userId ORDER BY startDate DESC")
    fun getByUser(userId: String): Flow<List<ReservationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: ReservationEntity)

    @Query("DELETE FROM reservations WHERE id = :id")
    suspend fun deleteById(id: String)
}
