package com.example.blueprint.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.blueprint.database.AppDatabase
import com.example.blueprint.database.dao.UserDao
import com.example.blueprint.database.entity.asDomainModel
import com.example.blueprint.domain.model.User
import com.example.blueprint.domain.model.asDatabaseModel

class UserManager(private val userDao: UserDao) {
    val user: LiveData<User> = Transformations.map(userDao.getUserLiveData()) {
        it?.asDomainModel()
    }

    /**
     * Get current signed in user if exist, else null
     */
    suspend fun getSignedUser(): User? = userDao.getUser()?.asDomainModel()

    /**
     * Insert New User
     */
    suspend fun insertUser(user: User) = userDao.insertUser(user.asDatabaseModel())

    /**
     * Remove signed in user
     */
    suspend fun removeUser() = userDao.removeUser()

    companion object {
        /**
         * Get an instance of UserManager with context
         */
        fun getRepo(context: Context): UserManager =
            UserManager(AppDatabase.getInstance(context).getUserDao())
    }
}
