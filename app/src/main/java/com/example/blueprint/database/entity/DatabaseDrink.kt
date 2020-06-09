package com.example.blueprint.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.blueprint.domain.model.Drink

@Entity
data class DatabaseDrink(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val price: Int
)

/**
 * Map DatabaseDrink to its domain model
 */
fun DatabaseDrink.asDomainModel(): Drink = Drink(
    name = name,
    price = price
)
