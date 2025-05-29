package com.jerdoul.foody.presentation.dashboard

import com.jerdoul.foody.domain.pojo.Dish
import com.jerdoul.foody.domain.pojo.DishType
import com.jerdoul.foody.ui.utils.UiText

data class DashboardState(
    val isLoading: Boolean = false,
    val searchQuery: String = "",
    val dishTypes: List<DishType> = emptyList(),
    val dishes: List<Dish> = emptyList(),
    val error: UiText? = null
)
