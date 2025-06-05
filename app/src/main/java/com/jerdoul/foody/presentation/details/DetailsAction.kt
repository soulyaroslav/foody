package com.jerdoul.foody.presentation.details

import com.jerdoul.foody.domain.pojo.Dish

sealed interface DetailsAction {
    data object Init : DetailsAction
    data object Increase : DetailsAction
    data object Decrease : DetailsAction
    data object AddToCart : DetailsAction
}
