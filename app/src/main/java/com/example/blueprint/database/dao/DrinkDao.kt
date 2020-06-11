package com.example.blueprint.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.blueprint.database.entity.DatabaseDrink

@Dao
interface DrinkDao {
    @Query("select * from DatabaseDrink")
    fun getDrinksLiveData(): LiveData<List<DatabaseDrink>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(drinks: List<DatabaseDrink>)
}
