package com.example.blueprint.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.blueprint.domain.model.User
import java.util.*

@Entity(tableName = "user")
data class DatabaseUser(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userName: String,
    val password: String,
    val dob: Date,
    val gender: Boolean
)

/**
 * Map DatabaseUser to its domain model
 */
fun DatabaseUser.asDomainModel(): User = User(
    userName = userName,
    password = password,
    dob = dob,
    gender = gender
)
