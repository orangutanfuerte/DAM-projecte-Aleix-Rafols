package com.example.travellikeasigma.domain

enum class AccessAction { LOGIN, LOGOUT }

interface AccessLogRepository {
    suspend fun logAccess(userId: String, action: AccessAction)
}
