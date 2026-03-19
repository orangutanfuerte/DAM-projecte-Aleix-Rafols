package com.example.travellikeasigma.di

import com.example.travellikeasigma.data.repository.TripRepositoryImpl
import com.example.travellikeasigma.data.repository.UserPreferencesRepositoryImpl
import com.example.travellikeasigma.domain.TripRepository
import com.example.travellikeasigma.domain.UserPreferencesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun bindTripRepository(impl: TripRepositoryImpl): TripRepository

    @Binds
    @Singleton
    abstract fun bindUserPreferencesRepository(impl: UserPreferencesRepositoryImpl): UserPreferencesRepository
}
