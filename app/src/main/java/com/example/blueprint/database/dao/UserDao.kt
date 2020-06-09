package com.example.blueprint.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.blueprint.database.entity.DatabaseUser

@Dao
interface UserDao {
    @Query("select * from user LIMIT 1")
    fun getUserLiveData(): LiveData<DatabaseUser>

    @Query("select * from user LIMIT 1")
    suspend fun getUser(): DatabaseUser

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: DatabaseUser)

    @Query("DELETE from user")
    suspend fun removeUser()

    @Transaction
    suspend fun insertUser(user: DatabaseUser) {
        removeUser()
        insert(user)
    }
}
