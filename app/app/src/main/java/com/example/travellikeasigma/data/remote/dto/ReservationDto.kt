package com.example.travellikeasigma.data.remote.dto

import com.example.travellikeasigma.domain.ApiReservation
import com.google.gson.annotations.SerializedName

data class ReservationDto(
    val id: String,
    @SerializedName("hotel_id")   val hotelId: String,
    @SerializedName("room_id")    val roomId: String,
    @SerializedName("start_date") val startDate: String,
    @SerializedName("end_date")   val endDate: String,
    @SerializedName("guest_name") val guestName: String,
    @SerializedName("guest_email") val guestEmail: String,
    val hotel: HotelDto? = null,
    val room: RoomDto? = null
)

data class ReserveResponseDto(
    val message: String,
    val nights: Int,
    val reservation: ReservationDto
)

data class ReservationsListDto(
    val reservations: List<ReservationDto>
)

fun ReservationDto.toDomain() = ApiReservation(
    id = id,
    hotelId = hotelId,
    roomId = roomId,
    startDate = startDate,
    endDate = endDate,
    guestName = guestName,
    guestEmail = guestEmail,
    hotel = hotel?.toDomain(),
    room = room?.toDomain()
)
