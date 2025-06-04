package com.jerdoul.foody.presentation.details

import com.jerdoul.foody.domain.pojo.Dish
import com.jerdoul.foody.ui.utils.UiText

data class DetailsState(
    val isLoading: Boolean = false,
    val dish: Dish? = null,
    val error: UiText? = null
)
