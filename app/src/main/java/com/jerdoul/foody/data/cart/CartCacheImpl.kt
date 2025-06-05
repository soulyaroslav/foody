package com.jerdoul.foody.data.cart

import com.jerdoul.foody.domain.cart.CartCache
import com.jerdoul.foody.domain.pojo.Dish
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject

class CartCacheImpl @Inject constructor() : CartCache {

    private val cart = mutableListOf<Dish>()
    private val mutex = Mutex()

    override suspend fun add(dish: Dish) {
        mutex.withLock {
            cart.add(dish)
        }
    }

    override suspend fun addBunch(dishes: List<Dish>) {
        mutex.withLock {
            cart.addAll(dishes)
        }
    }

    override suspend fun obtain(): List<Dish> {
        return mutex.withLock {
            cart.toList()
        }
    }

    override suspend fun remove(dish: Dish) {
        mutex.withLock {
            cart.remove(dish)
        }
    }

    override suspend fun clear() {
        mutex.withLock {
            if (cart.isNotEmpty()) {
                cart.clear()
            }
        }
    }

}