package com.jerdoul.foody.presentation.dashboard

import com.jerdoul.foody.domain.pojo.DishType

sealed interface DashboardAction {
    data object RetrieveData : DashboardAction
    data class Search(val query: String) : DashboardAction
    data class FilterDishes(val dishType: DishType?) : DashboardAction
}
