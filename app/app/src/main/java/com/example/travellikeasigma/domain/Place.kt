package com.example.travellikeasigma.model

enum class PlaceCategory { RESTAURANT, MUSEUM, PARK, TEMPLE, SHOPPING, LANDMARK, CAFE, OTHER }

data class Place(
    val id: Int,
    val name: String,
    val category: PlaceCategory,
    val address: String,
    val tripId: Int   // FK → Trip.id (future Room wiring)
)

val samplePlaces = listOf(
    Place(0, "Senso-ji Temple",         PlaceCategory.TEMPLE,     "Asakusa, Tokyo",    tripId = 1),
    Place(1, "Tsukiji Outer Market",    PlaceCategory.RESTAURANT, "Chuo, Tokyo",       tripId = 1),
    Place(2, "Shinjuku Gyoen",          PlaceCategory.PARK,       "Shinjuku, Tokyo",   tripId = 1),
    Place(3, "Shibuya Crossing",        PlaceCategory.LANDMARK,   "Shibuya, Tokyo",    tripId = 1),
    Place(4, "Meiji Shrine",            PlaceCategory.TEMPLE,     "Harajuku, Tokyo",   tripId = 1),
    Place(5, "Fushimi Inari Taisha",    PlaceCategory.TEMPLE,     "Fushimi, Kyoto",    tripId = 1),
    Place(6, "Arashiyama Bamboo Grove", PlaceCategory.PARK,       "Arashiyama, Kyoto", tripId = 1),
    Place(7, "Nishiki Market",          PlaceCategory.SHOPPING,   "Nakagyo, Kyoto",    tripId = 1),
    Place(8, "Kinkaku-ji",             PlaceCategory.LANDMARK,   "Kita, Kyoto",       tripId = 1),
)
