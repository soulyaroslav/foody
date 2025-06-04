package com.jerdoul.foody.domain.pojo

import kotlinx.serialization.Serializable

@Serializable
enum class DishType(val type: String) {
    FAST_FOOD("Fast Food"),
    FRESH_FRUITS("Fresh Fruits"),
    FRESH_VEGETABLES("Fresh Vegetables"),
    SPICY_FOOD("Spicy Food"),
    VEGETARIAN_FOOD("Vegetarian"),
    ORGANIC_FOOD("Organic Food")
}
