package com.jerdoul.foody.presentation.search

import com.jerdoul.foody.domain.pojo.Dish
import com.jerdoul.foody.domain.pojo.DishType
import com.jerdoul.foody.ui.utils.UiText

data class SearchState(
    val isLoading: Boolean = false,
    val searchQuery: String = "",
    val dishes: List<Dish> = emptyList(),
    val error: UiText? = null,
    val dishesCount: Int = 0
)
