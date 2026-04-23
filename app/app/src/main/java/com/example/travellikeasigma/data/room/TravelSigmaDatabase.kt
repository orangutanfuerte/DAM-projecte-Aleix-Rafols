package com.example.travellikeasigma.data.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [TripEntity::class, ActivityEntity::class, UserEntity::class, AccessLogEntity::class],
    version = 1,
    exportSchema = false
)
abstract class TravelSigmaDatabase : RoomDatabase() {
    abstract fun tripDao(): TripDao
    abstract fun activityDao(): ItineraryActivityDao
    abstract fun userDao(): UserDao
    abstract fun accessLogDao(): AccessLogDao
}
