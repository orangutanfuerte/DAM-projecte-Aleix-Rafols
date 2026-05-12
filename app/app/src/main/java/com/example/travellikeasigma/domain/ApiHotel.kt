package com.example.travellikeasigma.domain

data class ApiHotel(
    val id: String,
    val name: String,
    val address: String,
    val rating: Int,
    val imageUrl: String,
    val rooms: List<ApiRoom>
)

data class ApiRoom(
    val id: String,
    val roomType: String,
    val price: Double,
    val images: List<String>
)
