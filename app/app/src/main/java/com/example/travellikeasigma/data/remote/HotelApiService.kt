package com.example.travellikeasigma.data.remote

import com.example.travellikeasigma.data.remote.dto.AvailabilityResponseDto
import com.example.travellikeasigma.data.remote.dto.HotelDto
import com.example.travellikeasigma.data.remote.dto.ReservationDto
import com.example.travellikeasigma.data.remote.dto.ReservationsListDto
import com.example.travellikeasigma.data.remote.dto.ReserveRequestDto
import com.example.travellikeasigma.data.remote.dto.ReserveResponseDto
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface HotelApiService {

    @GET("hotels/{group_id}/hotels")
    suspend fun listHotels(
        @Path("group_id") groupId: String
    ): List<HotelDto>

    @GET("hotels/{group_id}/availability")
    suspend fun checkAvailability(
        @Path("group_id") groupId: String,
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String,
        @Query("city") city: String? = null
    ): AvailabilityResponseDto

    @POST("hotels/{group_id}/reserve")
    suspend fun reserve(
        @Path("group_id") groupId: String,
        @Body request: ReserveRequestDto
    ): ReserveResponseDto

    @GET("hotels/{group_id}/reservations")
    suspend fun listReservations(
        @Path("group_id") groupId: String,
        @Query("guest_email") guestEmail: String? = null
    ): ReservationsListDto

    @DELETE("reservations/{res_id}")
    suspend fun cancelReservation(
        @Path("res_id") resId: String
    ): ResponseBody
}
