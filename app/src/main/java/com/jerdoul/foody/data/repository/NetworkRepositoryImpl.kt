package com.jerdoul.foody.data.repository

import com.jerdoul.foody.domain.Result
import com.jerdoul.foody.domain.error.NetworkErrorHandler
import com.jerdoul.foody.domain.error.type.NetworkError
import com.jerdoul.foody.domain.pojo.Dish
import com.jerdoul.foody.domain.pojo.DishType
import com.jerdoul.foody.domain.pojo.IngredientType
import com.jerdoul.foody.domain.repository.NetworkRepository
import kotlinx.coroutines.delay
import javax.inject.Inject

private val dishes = listOf(
    Dish(
        id = 1,
        name = "Cheeseburger",
        shortDescription = "Juicy beef burger",
        description = "A juicy beef burger layered with melted cheddar cheese, crisp lettuce, fresh tomato, pickles, and a toasted sesame bun. Served hot with optional condiments.",
        calories = 750,
        type = DishType.FAST_FOOD,
        price = 5.99,
        cookTimeMinutes = "10–15 min",
        ingredients = listOf(
            IngredientType.MEAT,
            IngredientType.VEGETABLE,
            IngredientType.DAIRY,
            IngredientType.GRAIN,
            IngredientType.SAUCE
        )
    ),
    Dish(
        id = 2,
        name = "Banana",
        shortDescription = "Sweet yellow fruit",
        description = "A naturally sweet and creamy yellow fruit, packed with potassium and ideal as a quick energy snack or a breakfast add-on. No cooking required.",
        calories = 105,
        type = DishType.FRESH_FRUITS,
        price = 0.59,
        cookTimeMinutes = "0 min",
        ingredients = listOf(
            IngredientType.FRUIT
        )
    ),
    Dish(
        id = 3,
        name = "Carrot Sticks",
        shortDescription = "Crunchy orange snack",
        description = "Freshly sliced carrots, crunchy and rich in beta-carotene. A healthy snack option that pairs well with dips like hummus or ranch.",
        calories = 50,
        type = DishType.FRESH_VEGETABLES,
        price = 1.49,
        cookTimeMinutes = "0–5 min",
        ingredients = listOf(
            IngredientType.VEGETABLE
        )
    ),
    Dish(
        id = 4,
        name = "Spicy Tacos",
        shortDescription = "Hot beef tacos",
        description = "Tortillas filled with seasoned hot beef, spicy salsa, shredded lettuce, and cheese. A flavorful and fiery bite for spice lovers.",
        calories = 620,
        type = DishType.SPICY_FOOD,
        price = 6.49,
        cookTimeMinutes = "15–20 min",
        ingredients = listOf(
            IngredientType.MEAT,
            IngredientType.VEGETABLE,
            IngredientType.SPICE,
            IngredientType.DAIRY,
            IngredientType.GRAIN,
            IngredientType.SAUCE
        )
    ),
    Dish(
        id = 5,
        name = "Veggie Wrap",
        shortDescription = "Grilled veggie wrap",
        description = "A grilled wrap filled with assorted roasted vegetables, creamy hummus, and fresh greens, making it a wholesome vegetarian option.",
        calories = 410,
        type = DishType.VEGETARIAN_FOOD,
        price = 5.29,
        cookTimeMinutes = "10–15 min",
        ingredients = listOf(
            IngredientType.VEGETABLE,
            IngredientType.GRAIN,
            IngredientType.LEGUME,
            IngredientType.SAUCE
        )
    ),
    Dish(
        id = 6,
        name = "Organic Salad",
        shortDescription = "Fresh green mix",
        description = "A fresh blend of organic greens, cherry tomatoes, cucumbers, and olive oil dressing. A light, refreshing choice with clean ingredients.",
        calories = 200,
        type = DishType.ORGANIC_FOOD,
        price = 4.99,
        cookTimeMinutes = "5–10 min",
        ingredients = listOf(
            IngredientType.VEGETABLE,
            IngredientType.OIL,
            IngredientType.HERB
        )
    ),
    Dish(
        id = 7,
        name = "French Fries",
        shortDescription = "Crispy potato sticks",
        description = "Golden, crispy potato sticks fried to perfection and lightly salted. A classic fast food favorite served hot and crunchy.",
        calories = 450,
        type = DishType.FAST_FOOD,
        price = 2.99,
        cookTimeMinutes = "8–12 min",
        ingredients = listOf(
            IngredientType.VEGETABLE,
            IngredientType.OIL,
            IngredientType.SAUCE
        )
    ),
    Dish(
        id = 8,
        name = "Strawberries",
        shortDescription = "Sweet red berries",
        description = "Fresh, juicy red berries bursting with natural sweetness and antioxidants. Perfect as a snack, dessert, or smoothie addition.",
        calories = 60,
        type = DishType.FRESH_FRUITS,
        price = 3.49,
        cookTimeMinutes = "0 min",
        ingredients = listOf(
            IngredientType.FRUIT
        )
    ),
    Dish(
        id = 9,
        name = "Cucumber Slices",
        shortDescription = "Cool green veggie",
        description = "Cool and refreshing slices of cucumber, great for hydration and low-calorie snacking. Often served as a side or salad ingredient.",
        calories = 25,
        type = DishType.FRESH_VEGETABLES,
        price = 1.19,
        cookTimeMinutes = "0–5 min",
        ingredients = listOf(
            IngredientType.VEGETABLE
        )
    ),
    Dish(
        id = 10,
        name = "Spicy Ramen",
        shortDescription = "Hot noodle soup",
        description = "A bowl of steaming hot noodles in a rich and spicy broth, topped with boiled egg, scallions, and vegetables. Comfort food with a kick.",
        calories = 540,
        type = DishType.SPICY_FOOD,
        price = 4.79,
        cookTimeMinutes = "7–10 min",
        ingredients = listOf(
            IngredientType.GRAIN,
            IngredientType.EGG,
            IngredientType.VEGETABLE,
            IngredientType.SPICE,
            IngredientType.SAUCE
        )
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