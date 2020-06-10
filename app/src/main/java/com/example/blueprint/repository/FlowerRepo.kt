package com.example.blueprint.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.blueprint.database.dao.FlowerDao
import com.example.blueprint.database.entity.asDomainModel
import com.example.blueprint.domain.model.Flower

class FlowerRepo(val dao: FlowerDao) {
    fun getAll(): LiveData<List<Flower>> = Transformations.map(dao.getFlowersLiveData()) {
        it?.map { flower -> flower.asDomainModel() }
    }
}
