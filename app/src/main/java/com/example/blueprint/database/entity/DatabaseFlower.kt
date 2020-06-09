package com.example.blueprint.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.blueprint.domain.model.Flower

@Entity
data class DatabaseFlower(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val price: Int
)

/**
 * Map DatabaseFlower to its domain model
 */
fun DatabaseFlower.asDomainModel(): Flower = Flower(
    name = name,
    price = price
)
