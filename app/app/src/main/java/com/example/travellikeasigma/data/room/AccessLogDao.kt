package com.example.travellikeasigma.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface AccessLogDao {
    @Insert
    suspend fun insertLog(log: AccessLogEntity)

    @Query("SELECT * FROM access_log WHERE userId = :uid ORDER BY dateTime DESC")
    suspend fun getLogsForUser(uid: String): List<AccessLogEntity>
}
