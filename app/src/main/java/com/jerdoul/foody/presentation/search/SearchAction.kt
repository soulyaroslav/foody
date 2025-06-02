package com.jerdoul.foody.presentation.search

import com.jerdoul.foody.domain.pojo.DishType

sealed interface SearchAction {
    data object RetrieveData : SearchAction
    data class Search(val query: String) : SearchAction
}
