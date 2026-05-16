package com.example.travellikeasigma.data.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [TripEntity::class, ActivityEntity::class, UserEntity::class, AccessLogEntity::class, ReservationEntity::class],
    version = 5,
    exportSchema = false
)
abstract class TravelSigmaDatabase : RoomDatabase() {
    abstract fun tripDao(): TripDao
    abstract fun activityDao(): ItineraryActivityDao
    abstract fun userDao(): UserDao
    abstract fun accessLogDao(): AccessLogDao
    abstract fun reservationDao(): ReservationDao
}
