package com.example.travellikeasigma.model

data class PackingItem(
    val id: Int,
    val name: String,
    val isPacked: Boolean = false
)

data class PackingCategory(
    val id: Int,
    val name: String,
    val emoji: String,
    val items: List<PackingItem>
)

/**
 * Represents a packing list for a trip.
 * [tripId] is a foreign key to [Trip] — null until Room is wired up.
 */
data class PackingList(
    val id: Int,
    val tripId: Int?,
    val categories: List<PackingCategory>
)

val samplePackingList = PackingList(
    id = 1,
    tripId = null,
    categories = listOf(
        PackingCategory(
            id = 1,
            name = "Health & Toiletries",
            emoji = "🧴",
            items = listOf(
                PackingItem(1,  "Toothbrush",       isPacked = true),
                PackingItem(2,  "Toothpaste",        isPacked = true),
                PackingItem(3,  "Sunscreen"),
                PackingItem(4,  "Medications"),
                PackingItem(5,  "Hand sanitiser")
            )
        ),
        PackingCategory(
            id = 2,
            name = "Electronics",
            emoji = "🔌",
            items = listOf(
                PackingItem(6,  "Phone charger",    isPacked = true),
                PackingItem(7,  "Power bank"),
                PackingItem(8,  "Camera"),
                PackingItem(9,  "Adapter plug"),
                PackingItem(10, "Headphones")
            )
        ),
        PackingCategory(
            id = 3,
            name = "Clothing",
            emoji = "👕",
            items = listOf(
                PackingItem(11, "T-shirts"),
                PackingItem(12, "Underwear"),
                PackingItem(13, "Socks"),
                PackingItem(14, "Jacket"),
                PackingItem(15, "Swimwear")
            )
        ),
        PackingCategory(
            id = 4,
            name = "Documents",
            emoji = "📄",
            items = listOf(
                PackingItem(16, "Passport",         isPacked = true),
                PackingItem(17, "Travel insurance"),
                PackingItem(18, "Hotel confirmation")
            )
        )
    )
)
