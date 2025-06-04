package com.jerdoul.foody.domain.pojo

import kotlinx.serialization.Serializable

@Serializable
data class Dish(
    val id: Int,
    val name: String,
    val description: String,
    val calories: Int,
    val type: DishType,
    val price: String
)
