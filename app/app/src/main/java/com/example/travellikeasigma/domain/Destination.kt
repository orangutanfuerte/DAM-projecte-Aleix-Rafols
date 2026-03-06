package com.example.travellikeasigma.model

data class Destination(val id: Int, val destinationName: String)

val sampleDestinations = listOf(
    Destination(1, "Japan"),
    Destination(2, "Iceland"),
    Destination(3, "Italy"),
    Destination(4, "Spain"),
    Destination(5, "Brasil")
)
