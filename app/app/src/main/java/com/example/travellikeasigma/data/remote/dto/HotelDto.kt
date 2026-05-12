package com.example.travellikeasigma.data.remote.dto

import com.example.travellikeasigma.domain.ApiHotel
import com.example.travellikeasigma.domain.ApiRoom
import com.google.gson.annotations.SerializedName

data class HotelDto(
    val id: String,
    val name: String,
    val address: String,
    val rating: Int,
    val rooms: List<RoomDto>?,   // nullable: el objeto hotel embebido en reservas no incluye rooms
    @SerializedName("image_url") val imageUrl: String
)

data class RoomDto(
    val id: String,
    @SerializedName("room_type") val roomType: String,
    val price: Double,
    val images: List<String>
)

fun HotelDto.toDomain() = ApiHotel(
    id = id,
    name = name,
    address = address,
    rating = rating,
    imageUrl = imageUrl,
    rooms = rooms?.map { it.toDomain() } ?: emptyList()
)

fun RoomDto.toDomain() = ApiRoom(
    id = id,
    roomType = roomType,
    price = price,
    images = images
)
