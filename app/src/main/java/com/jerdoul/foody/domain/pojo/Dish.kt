package com.jerdoul.foody.domain.pojo

data class Dish(
    val name: String,
    val description: String,
    val calories: Int,
    val type: DishType,
    val price: String
)
