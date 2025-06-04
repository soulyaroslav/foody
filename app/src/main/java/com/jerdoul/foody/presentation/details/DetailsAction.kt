package com.jerdoul.foody.presentation.details

sealed interface DetailsAction {
    data object Init : DetailsAction
}
