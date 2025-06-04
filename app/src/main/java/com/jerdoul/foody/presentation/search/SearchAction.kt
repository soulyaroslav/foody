package com.jerdoul.foody.presentation.search

sealed interface SearchAction {
    data object RetrieveData : SearchAction
    data class Search(val query: String) : SearchAction
}
