package com.jerdoul.foody.presentation.cart

import com.jerdoul.foody.domain.pojo.Dish
import com.jerdoul.foody.presentation.details.DetailsAction

sealed interface CartAction {
    data object RetrieveCart : CartAction
    data class Increase(val id: Int) : CartAction
    data class Decrease(val id: Int) : CartAction
    data class Remove(val dish: Dish) : CartAction
}
