package com.jerdoul.foody.presentation.cart

import com.jerdoul.foody.domain.pojo.Dish
import com.jerdoul.foody.ui.utils.UiText

data class CartState(
    val isLoading: Boolean = false,
    val dishStates: List<DishState> = emptyList(),
    val error: UiText? = null
)

data class DishState(
    val dish: Dish,
    val selectedCount: Int
)
