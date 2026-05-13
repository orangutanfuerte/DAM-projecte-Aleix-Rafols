package com.example.travellikeasigma.di

import android.content.Context
import androidx.room.Room
import com.example.travellikeasigma.data.remote.HotelApiService
import com.example.travellikeasigma.data.repository.AccessLogRepositoryImpl
import com.example.travellikeasigma.data.repository.AuthRepositoryImpl
import com.example.travellikeasigma.data.repository.HotelRepositoryImpl
import com.example.travellikeasigma.data.repository.RoomTripRepositoryImpl
import com.example.travellikeasigma.data.repository.UserPreferencesRepositoryImpl
import com.example.travellikeasigma.data.repository.UserRepositoryImpl
import com.example.travellikeasigma.data.room.AccessLogDao
import com.example.travellikeasigma.data.room.ItineraryActivityDao
import com.example.travellikeasigma.data.room.TravelSigmaDatabase
import com.example.travellikeasigma.data.room.TripDao
import com.example.travellikeasigma.data.room.UserDao
import com.example.travellikeasigma.domain.AccessLogRepository
import com.example.travellikeasigma.domain.AuthRepository
import com.example.travellikeasigma.domain.HotelRepository
import com.example.travellikeasigma.domain.TripRepository
import com.example.travellikeasigma.domain.UserPreferencesRepository
import com.example.travellikeasigma.domain.UserRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()
}

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): TravelSigmaDatabase =
        Room.databaseBuilder(context, TravelSigmaDatabase::class.java, "travel_sigma_db")
            .fallbackToDestructiveMigration(dropAllTables = true)
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
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl("http://15.224.84.148:8090/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideHotelApiService(retrofit: Retrofit): HotelApiService =
        retrofit.create(HotelApiService::class.java)
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

    @Binds
    @Singleton
    abstract fun bindHotelRepository(impl: HotelRepositoryImpl): HotelRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository
}
