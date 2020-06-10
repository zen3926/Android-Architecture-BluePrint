package com.example.blueprint.domain.model

data class Drink(
    val name: String,
    val price: Int
)

/**
 * Map Drink to Product
 */
fun Drink.asProduct(): Product = Product(
    name = name,
    price = price,
    imageUrl = "https://cdn.iconscout.com/icon/premium/png-256-thumb/glass-of-water-1509805-1275100.png"
)
