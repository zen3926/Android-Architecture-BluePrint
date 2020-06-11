package com.example.blueprint.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.blueprint.database.entity.DatabaseFlower

@Dao
interface FlowerDao {
    @Query("select * from DatabaseFlower")
    fun getFlowersLiveData(): LiveData<List<DatabaseFlower>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(flowers: List<DatabaseFlower>)
}
