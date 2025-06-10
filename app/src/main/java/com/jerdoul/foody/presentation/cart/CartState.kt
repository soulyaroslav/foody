package com.jerdoul.foody.presentation.cart

import com.jerdoul.foody.domain.pojo.Dish
import com.jerdoul.foody.ui.utils.UiText

data class CartState(
    val isLoading: Boolean = false,
    val dishes: List<Dish> = emptyList(),
    val dishCounts: List<Int> = emptyList(),
    val error: UiText? = null,
    val itemCount: Int = 0,
    val itemsPrice: Double = 0.0,
    val totalPrice: Double = 0.0
)
