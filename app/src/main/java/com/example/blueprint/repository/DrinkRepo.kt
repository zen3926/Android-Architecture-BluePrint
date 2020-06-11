package com.example.blueprint.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.blueprint.database.dao.DrinkDao
import com.example.blueprint.database.entity.asDomainModel
import com.example.blueprint.domain.model.Drink

class DrinkRepo(val dao: DrinkDao) {
    /**
     * Transformation map of database model to its domain model
     */
    fun getAll(): LiveData<List<Drink>> = Transformations.map(dao.getDrinksLiveData()) {
        it?.map { drink -> drink.asDomainModel() }
    }
}
