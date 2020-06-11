package com.example.blueprint.network.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkUser(
    val userId: Int,
    val id: Int,
    val title: String?,
    val completed: Boolean
)


