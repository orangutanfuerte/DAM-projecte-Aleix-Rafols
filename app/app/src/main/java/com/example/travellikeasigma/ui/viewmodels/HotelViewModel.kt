package com.example.travellikeasigma.ui.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travellikeasigma.domain.ApiHotel
import com.example.travellikeasigma.domain.ApiRoom
import com.example.travellikeasigma.domain.HotelRepository
import com.example.travellikeasigma.domain.LocalReservation
import com.example.travellikeasigma.domain.ReservationRepository
import com.example.travellikeasigma.domain.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HotelViewModel @Inject constructor(
    private val hotelRepository: HotelRepository,
    private val reservationRepository: ReservationRepository,
    private val prefsRepository: UserPreferencesRepository
) : ViewModel() {

    companion object {
        const val BASE_IMAGE_URL = "http://15.224.84.148:8090"
        private const val GROUP_ID = "G13"
        private const val TAG = "HotelViewModel"
    }

    private val userId: String = prefsRepository.getLoggedInUid() ?: ""

    var availableHotels by mutableStateOf<List<ApiHotel>>(emptyList())
        private set
    var isSearchLoading by mutableStateOf(false)
        private set
    var searchError by mutableStateOf<String?>(null)
        private set
    var isBookingLoading by mutableStateOf(false)
        private set
    var bookingError by mutableStateOf<String?>(null)
        private set
    var isCancelLoading by mutableStateOf(false)
        private set

    val reservations: StateFlow<List<LocalReservation>> = reservationRepository
        .getReservations(userId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun searchHotels(city: String, startDate: String, endDate: String) {
        viewModelScope.launch {
            isSearchLoading = true
            searchError = null
            try {
                Log.d(TAG, "searchHotels: city=$city, $startDate → $endDate")
                availableHotels = hotelRepository.checkAvailability(GROUP_ID, startDate, endDate, city.lowercase())
                Log.i(TAG, "searchHotels: found ${availableHotels.size} hotels")
            } catch (e: Exception) {
                Log.e(TAG, "searchHotels: failed — ${e.message}")
                searchError = e.message
                availableHotels = emptyList()
            } finally {
                isSearchLoading = false
            }
        }
    }

    fun bookRoom(
        hotel: ApiHotel,
        room: ApiRoom,
        startDate: String,
        endDate: String,
        guestName: String,
        guestEmail: String,
        persons: Int,
        tripName: String? = null,
        onSuccess: (LocalReservation) -> Unit,
        onError: () -> Unit
    ) {
        viewModelScope.launch {
            isBookingLoading = true
            bookingError = null
            try {
                Log.d(TAG, "bookRoom: hotel=${hotel.id}, room=${room.id}, guest=$guestEmail, trip=$tripName")
                val apiReservation = hotelRepository.reserve(
                    GROUP_ID, hotel.id, room.id, startDate, endDate, guestName, guestEmail
                )
                val local = LocalReservation(
                    id = apiReservation.id,
                    hotelId = hotel.id,
                    hotelName = hotel.name,
                    hotelAddress = hotel.address,
                    hotelRating = hotel.rating,
                    hotelImageUrl = hotel.imageUrl,
                    roomId = room.id,
                    roomType = room.roomType,
                    pricePerNight = room.price,
                    roomImages = room.images,
                    startDate = startDate,
                    endDate = endDate,
                    guestName = guestName,
                    guestEmail = guestEmail,
                    persons = persons,
                    tripName = tripName
                )
                reservationRepository.saveReservation(local, userId)
                Log.i(TAG, "bookRoom: reservation ${apiReservation.id} saved")
                onSuccess(local)
            } catch (e: Exception) {
                Log.e(TAG, "bookRoom: failed — ${e.message}")
                bookingError = e.message
                onError()
            } finally {
                isBookingLoading = false
            }
        }
    }

    fun cancelReservation(
        reservation: LocalReservation,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        viewModelScope.launch {
            isCancelLoading = true
            try {
                try {
                    hotelRepository.cancelReservation(reservation.id)
                    Log.i(TAG, "cancelReservation: API cancelled ${reservation.id}")
                } catch (e: Exception) {
                    Log.w(TAG, "cancelReservation: API call failed — ${e.message}, deleting locally anyway")
                }
                reservationRepository.deleteReservation(reservation.id)
                Log.i(TAG, "cancelReservation: local reservation ${reservation.id} removed")
                onSuccess()
            } catch (e: Exception) {
                Log.e(TAG, "cancelReservation: failed — ${e.message}")
                onError()
            } finally {
                isCancelLoading = false
            }
        }
    }

    fun clearSearch() {
        availableHotels = emptyList()
        searchError = null
        bookingError = null
    }
}
