package com.jerdoul.foody.domain.repository

import com.jerdoul.foody.domain.Result
import com.jerdoul.foody.domain.error.type.NetworkError
import com.jerdoul.foody.domain.pojo.DishType
import com.jerdoul.foody.domain.pojo.Dish

interface NetworkRepository {
    suspend fun login(email: String, password: String): Result<Boolean, NetworkError>
    suspend fun register(
        name: String,
        email: String,
        password: String,
        configPassword: String
    ): Result<Boolean, NetworkError>

    suspend fun retrieveDishTypes(): Result<List<DishType>, NetworkError>
    suspend fun retrieveDishes(): Result<List<Dish>, NetworkError>
}