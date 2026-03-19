package com.example.travellikeasigma.domain

enum class PlaceCategory { RESTAURANT, MUSEUM, PARK, TEMPLE, SHOPPING, LANDMARK, CAFE, OTHER }

data class Place(
    val id: Int,
    val name: String,
    val category: PlaceCategory,
    val address: String
)

val icelandPlaces: List<Place> = emptyList()
val italyPlaces: List<Place> = emptyList()

val samplePlaces = listOf(
    Place(1, "Senso-ji Temple",         PlaceCategory.TEMPLE,     "Asakusa, Tokyo"),
    Place(2, "Tsukiji Outer Market",    PlaceCategory.RESTAURANT, "Chuo, Tokyo"),
    Place(3, "Shinjuku Gyoen",          PlaceCategory.PARK,       "Shinjuku, Tokyo"),
    Place(4, "Shibuya Crossing",        PlaceCategory.LANDMARK,   "Shibuya, Tokyo"),
    Place(5, "Meiji Shrine",            PlaceCategory.TEMPLE,     "Harajuku, Tokyo"),
    Place(6, "Fushimi Inari Taisha",    PlaceCategory.TEMPLE,     "Fushimi, Kyoto"),
    Place(7, "Arashiyama Bamboo Grove", PlaceCategory.PARK,       "Arashiyama, Kyoto"),
    Place(8, "Nishiki Market",          PlaceCategory.SHOPPING,   "Nakagyo, Kyoto"),
    Place(9, "Kinkaku-ji",             PlaceCategory.LANDMARK,   "Kita, Kyoto"),
)
