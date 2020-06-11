package com.example.blueprint.network

import com.example.blueprint.network.model.NetworkUser
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

interface SimpleService {
    @GET("todos/1")
    suspend fun getSample(): Response<NetworkUser>
}

object SimpleNetwork {
    // Configure retrofit to parse Json and use coroutines
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://jsonplaceholder.typicode.com/")
        .addConverterFactory(MoshiConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()

    val simpleService: SimpleService = retrofit.create(SimpleService::class.java)
}
