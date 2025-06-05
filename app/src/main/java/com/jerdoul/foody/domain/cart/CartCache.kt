package com.jerdoul.foody.domain.cart

import com.jerdoul.foody.domain.pojo.Dish

interface CartCache {
    suspend fun add(dish: Dish)
    suspend fun addBunch(dishes: List<Dish>)
    suspend fun obtain(): List<Dish>
    suspend fun remove(dish: Dish)
    suspend fun clear()
}