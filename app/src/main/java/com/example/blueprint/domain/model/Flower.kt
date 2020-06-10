package com.example.blueprint.domain.model

data class Flower(
    val name: String,
    val price: Int
)

/**
 * Map Flower to Product
 */
fun Flower.asProduct(): Product = Product(
    name = name,
    price = price,
    imageUrl = "https://i.pinimg.com/originals/36/a4/29/36a4291e03ebf546124fa0e7b5a296ba.png"
)
