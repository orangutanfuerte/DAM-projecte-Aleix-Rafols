package com.example.travellikeasigma.data.repository

import com.example.travellikeasigma.data.room.AccessLogDao
import com.example.travellikeasigma.data.room.AccessLogEntity
import com.example.travellikeasigma.domain.AccessAction
import com.example.travellikeasigma.domain.AccessLogRepository
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccessLogRepositoryImpl @Inject constructor(
    private val accessLogDao: AccessLogDao
) : AccessLogRepository {

    override suspend fun logAccess(userId: String, action: AccessAction) {
        accessLogDao.insertLog(
            AccessLogEntity(
                userId = userId,
                action = action.name,
                dateTime = LocalDateTime.now().toString()
            )
        )
    }
}
