package com.jerdoul.foody.data.repository

import com.jerdoul.foody.domain.Result
import com.jerdoul.foody.domain.error.NetworkErrorHandler
import com.jerdoul.foody.domain.error.type.NetworkError
import com.jerdoul.foody.domain.pojo.Dish
import com.jerdoul.foody.domain.pojo.DishType
import com.jerdoul.foody.domain.repository.NetworkRepository
import kotlinx.coroutines.delay
import javax.inject.Inject

private val dishes = listOf(
    Dish(
        id = 1,
        name = "Cheeseburger",
        description = "Juicy beef burger",
        calories = 750,
        type = DishType.FAST_FOOD,
        price = "$5.99"
    ),
    Dish(
        id = 2,
        name = "Banana",
        description = "Sweet yellow fruit",
        calories = 105,
        type = DishType.FRESH_FRUITS,
        price = "$0.59"
    ),
    Dish(
        id = 3,
        name = "Carrot Sticks",
        description = "Crunchy orange snack",
        calories = 50,
        type = DishType.FRESH_VEGETABLES,
        price = "$1.49"
    ),
    Dish(
        id = 4,
        name = "Spicy Tacos",
        description = "Hot beef tacos",
        calories = 620,
        type = DishType.SPICY_FOOD,
        price = "$6.49"
    ),
    Dish(
        id = 5,
        name = "Veggie Wrap",
        description = "Grilled veggie wrap",
        calories = 410,
        type = DishType.VEGETARIAN_FOOD,
        price = "$5.29"
    ),
    Dish(
        id = 6,
        name = "Organic Salad",
        description = "Fresh green mix",
        calories = 200,
        type = DishType.ORGANIC_FOOD,
        price = "$4.99"
    ),
    Dish(
        id = 7,
        name = "French Fries",
        description = "Crispy potato sticks",
        calories = 450,
        type = DishType.FAST_FOOD,
        price = "$2.99"
    ),
    Dish(
        id = 8,
        name = "Strawberries",
        description = "Sweet red berries",
        calories = 60,
        type = DishType.FRESH_FRUITS,
        price = "$3.49"
    ),
    Dish(
        id = 9,
        name = "Cucumber Slices",
        description = "Cool green veggie",
        calories = 25,
        type = DishType.FRESH_VEGETABLES,
        price = "$1.19"
    ),
    Dish(
        id = 10,
        name = "Spicy Ramen",
        description = "Hot noodle soup",
        calories = 540,
        type = DishType.SPICY_FOOD,
        price = "$4.79"
    )
)

class NetworkRepositoryImpl @Inject constructor(private val networkErrorHandler: NetworkErrorHandler) : NetworkRepository {
    override suspend fun login(email: String, password: String): Result<Boolean, NetworkError> =
        networkErrorHandler.runWithErrorHandler {
            delay(300L)
            true
        }

    override suspend fun register(
        name: String,
        email: String,
        password: String,
        configPassword: String
    ): Result<Boolean, NetworkError> =
        networkErrorHandler.runWithErrorHandler {
            delay(300L)
            true
        }

    override suspend fun retrieveDishTypes(): Result<List<DishType>, NetworkError> =
        networkErrorHandler.runWithErrorHandler {
            delay(1000L)
            listOf(
                DishType.ORGANIC_FOOD,
                DishType.VEGETARIAN_FOOD,
                DishType.FRESH_FRUITS,
                DishType.FRESH_VEGETABLES,
                DishType.FAST_FOOD,
                DishType.SPICY_FOOD
            )
        }

    override suspend fun retrieveDishes(): Result<List<Dish>, NetworkError> =
        networkErrorHandler.runWithErrorHandler {
            dishes
        }

    override suspend fun retrieveDishById(id: Int): Result<Dish, NetworkError> =
        networkErrorHandler.runWithErrorHandler {
            dishes.first { it.id == id }
        }
}