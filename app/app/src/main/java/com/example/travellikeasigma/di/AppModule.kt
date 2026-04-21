package com.example.travellikeasigma.di

import android.content.Context
import androidx.room.Room
import com.example.travellikeasigma.data.repository.AccessLogRepositoryImpl
import com.example.travellikeasigma.data.repository.RoomTripRepositoryImpl
import com.example.travellikeasigma.data.repository.UserPreferencesRepositoryImpl
import com.example.travellikeasigma.data.repository.UserRepositoryImpl
import com.example.travellikeasigma.data.room.AccessLogDao
import com.example.travellikeasigma.data.room.ItineraryActivityDao
import com.example.travellikeasigma.data.room.TravelSigmaDatabase
import com.example.travellikeasigma.data.room.TripDao
import com.example.travellikeasigma.data.room.UserDao
import com.example.travellikeasigma.domain.AccessLogRepository
import com.example.travellikeasigma.domain.TripRepository
import com.example.travellikeasigma.domain.UserPreferencesRepository
import com.example.travellikeasigma.domain.UserRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): TravelSigmaDatabase =
        Room.databaseBuilder(context, TravelSigmaDatabase::class.java, "travel_sigma_db")
            .addMigrations(TravelSigmaDatabase.MIGRATION_1_2)
            .build()

    @Provides
    @Singleton
    fun provideTripDao(db: TravelSigmaDatabase): TripDao = db.tripDao()

    @Provides
    @Singleton
    fun provideActivityDao(db: TravelSigmaDatabase): ItineraryActivityDao = db.activityDao()

    @Provides
    @Singleton
    fun provideUserDao(db: TravelSigmaDatabase): UserDao = db.userDao()

    @Provides
    @Singleton
    fun provideAccessLogDao(db: TravelSigmaDatabase): AccessLogDao = db.accessLogDao()
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindTripRepository(impl: RoomTripRepositoryImpl): TripRepository

    @Binds
    @Singleton
    abstract fun bindUserPreferencesRepository(impl: UserPreferencesRepositoryImpl): UserPreferencesRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(impl: UserRepositoryImpl): UserRepository

    @Binds
    @Singleton
    abstract fun bindAccessLogRepository(impl: AccessLogRepositoryImpl): AccessLogRepository
}
