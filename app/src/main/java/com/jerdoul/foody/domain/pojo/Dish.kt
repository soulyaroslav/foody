package com.jerdoul.foody.domain.pojo

data class Dish(
    val id: Int,
    val name: String,
    val shortDescription: String,
    val description: String,
    val calories: Int,
    val type: DishType,
    val price: Double,
    val cookTimeMinutes: String,
    val ingredients: List<IngredientType>,
    val rating: String = "2.6"
)

enum class IngredientType {
    VEGETABLE,
    FRUIT,
    MEAT,
    SEAFOOD,
    DAIRY,
    GRAIN,
    NUT,
    LEGUME,
    SPICE,
    HERB,
    SWEETENER,
    OIL,
    SAUCE,
    EGG,
    BEVERAGE,
    MUSHROOM,
    PLANT_BASED,
    PROCESSED,
    OTHER
}

fun Dish?.identifier() = this?.let { "$id-$name" } ?: ""
