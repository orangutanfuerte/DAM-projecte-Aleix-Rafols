package com.example.travellikeasigma.model

data class PackingItem(
    val name: String,
    val isPacked: Boolean = false
)

data class PackingCategory(
    val name: String,
    val emoji: String,
    val items: List<PackingItem>
)

val samplePackingCategories = listOf(
    PackingCategory(
        name = "Health & Toiletries",
        emoji = "🧴",
        items = listOf(
            PackingItem("Toothbrush",       isPacked = true),
            PackingItem("Toothpaste",        isPacked = true),
            PackingItem("Sunscreen"),
            PackingItem("Medications"),
            PackingItem("Hand sanitiser")
        )
    ),
    PackingCategory(
        name = "Electronics",
        emoji = "🔌",
        items = listOf(
            PackingItem("Phone charger",    isPacked = true),
            PackingItem("Power bank"),
            PackingItem("Camera"),
            PackingItem("Adapter plug"),
            PackingItem("Headphones")
        )
    ),
    PackingCategory(
        name = "Clothing",
        emoji = "👕",
        items = listOf(
            PackingItem("T-shirts"),
            PackingItem("Underwear"),
            PackingItem("Socks"),
            PackingItem("Jacket"),
            PackingItem("Swimwear")
        )
    ),
    PackingCategory(
        name = "Documents",
        emoji = "📄",
        items = listOf(
            PackingItem("Passport",         isPacked = true),
            PackingItem("Travel insurance"),
            PackingItem("Hotel confirmation")
        )
    )
)
