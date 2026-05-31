package dev.jianastrero.journey.example.model

data class Listing(
    val id: String,
    val title: String,
    val description: String,
    val price: Double,
    val category: String,
    val sellerId: String,
)
