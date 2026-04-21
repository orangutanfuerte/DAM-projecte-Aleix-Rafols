package com.example.travellikeasigma.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [TripEntity::class, ActivityEntity::class, UserEntity::class, AccessLogEntity::class],
    version = 2,
    exportSchema = false
)
abstract class TravelSigmaDatabase : RoomDatabase() {
    abstract fun tripDao(): TripDao
    abstract fun activityDao(): ItineraryActivityDao
    abstract fun userDao(): UserDao
    abstract fun accessLogDao(): AccessLogDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE trips ADD COLUMN userId TEXT NOT NULL DEFAULT ''")
                db.execSQL("""
                    CREATE TABLE users (
                        userId TEXT PRIMARY KEY NOT NULL,
                        email TEXT NOT NULL,
                        username TEXT NOT NULL,
                        dateOfBirth TEXT NOT NULL,
                        language TEXT NOT NULL DEFAULT 'en',
                        theme TEXT NOT NULL DEFAULT 'system',
                        notificationsEnabled INTEGER NOT NULL DEFAULT 1
                    )
                """.trimIndent())
                db.execSQL("""
                    CREATE TABLE access_log (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        userId TEXT NOT NULL,
                        action TEXT NOT NULL,
                        dateTime TEXT NOT NULL
                    )
                """.trimIndent())
            }
        }
    }
}
