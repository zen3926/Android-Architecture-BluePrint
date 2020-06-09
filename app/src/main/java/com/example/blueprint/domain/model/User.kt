package com.example.blueprint.domain.model

import com.example.blueprint.database.entity.DatabaseUser
import java.util.*

data class User(
    val userName: String,
    val password: String,
    val dob: Date,
    val gender: Boolean
)

fun User.asDatabaseModel(): DatabaseUser = DatabaseUser(
    userName = userName,
    password = password,
    dob = dob,
    gender = gender
)
