package com.example.travellikeasigma.domain

data class Destination(val id: Int, val destinationName: String)

val sampleDestinations = listOf(
    Destination(1, "Barcelona"),
    Destination(2, "London"),
    Destination(3, "Paris")
)
