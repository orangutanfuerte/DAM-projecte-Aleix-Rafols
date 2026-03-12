package com.example.travellikeasigma.domain

data class Hotel(
    val id: Int,
    val name: String,
    val pricePerNight: Double,
    val destination: Destination
)

val sampleHotels = listOf(
    Hotel(1, "Papa Hotel",   89.0,  sampleDestinations[0]),
    Hotel(2, "Pepe Hotel",  124.0,  sampleDestinations[0]),
    Hotel(3, "Pipi Hotel",   67.0,  sampleDestinations[0]),
    Hotel(4, "Popo Hotel",  210.0,  sampleDestinations[0]),
    Hotel(5, "Pupu Hotel",  155.0,  sampleDestinations[0])
)
