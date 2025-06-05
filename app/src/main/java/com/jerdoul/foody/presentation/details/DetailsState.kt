package com.jerdoul.foody.presentation.details

import com.jerdoul.foody.domain.pojo.Dish
import com.jerdoul.foody.ui.utils.UiText

data class DetailsState(
    val isLoading: Boolean = false,
    val dish: Dish? = null,
    val cartCount: Int = 0,
    val selectedCount: Int = 1,
    val error: UiText? = null
)
