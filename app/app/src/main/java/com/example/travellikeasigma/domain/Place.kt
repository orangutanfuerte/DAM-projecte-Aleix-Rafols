package com.example.travellikeasigma.model

enum class PlaceCategory { RESTAURANT, MUSEUM, PARK, TEMPLE, SHOPPING, LANDMARK, CAFE, OTHER }

data class Place(
    val name: String,
    val category: PlaceCategory,
    val address: String
)

val samplePlaces = listOf(
    Place("Senso-ji Temple",         PlaceCategory.TEMPLE,     "Asakusa, Tokyo"),
    Place("Tsukiji Outer Market",    PlaceCategory.RESTAURANT, "Chuo, Tokyo"),
    Place("Shinjuku Gyoen",          PlaceCategory.PARK,       "Shinjuku, Tokyo"),
    Place("Shibuya Crossing",        PlaceCategory.LANDMARK,   "Shibuya, Tokyo"),
    Place("Meiji Shrine",            PlaceCategory.TEMPLE,     "Harajuku, Tokyo"),
    Place("Fushimi Inari Taisha",    PlaceCategory.TEMPLE,     "Fushimi, Kyoto"),
    Place("Arashiyama Bamboo Grove", PlaceCategory.PARK,       "Arashiyama, Kyoto"),
    Place("Nishiki Market",          PlaceCategory.SHOPPING,   "Nakagyo, Kyoto"),
    Place("Kinkaku-ji",             PlaceCategory.LANDMARK,   "Kita, Kyoto"),
)
