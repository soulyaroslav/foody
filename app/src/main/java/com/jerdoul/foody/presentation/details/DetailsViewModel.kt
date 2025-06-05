package com.jerdoul.foody.presentation.details

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jerdoul.foody.domain.Result
import com.jerdoul.foody.domain.cart.CartCache
import com.jerdoul.foody.domain.pojo.Dish
import com.jerdoul.foody.domain.usecase.RetrieveDishByIdUseCase
import com.jerdoul.foody.presentation.asErrorUiText
import com.jerdoul.foody.presentation.navigation.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val retrieveDishUseCase: RetrieveDishByIdUseCase,
    private val navigator: Navigator,
    private val cartCache: CartCache,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(DetailsState())
    val state = _state.asStateFlow()

    init {
        onAction(DetailsAction.Init)
    }

    fun onAction(action: DetailsAction) {
        when (action) {
            DetailsAction.Init -> init()
            is DetailsAction.AddToCart -> addToCart()
            DetailsAction.Decrease -> decrease()
            DetailsAction.Increase -> increase()
        }
    }

    private fun addToCart() = with(_state.value) {
        dish?.let { dish ->
            viewModelScope.launch {
                cartCache.clear()
                if (selectedCount > 1) {
                    val dishes = List(selectedCount) { dish }
                    cartCache.addBunch(dishes)
                } else {
                    cartCache.add(dish)
                }
                val count = cartCache.obtain().size
                _state.update {
                    it.copy(cartCount = count)
                }
            }
        }
    }

    private fun increase() {
        _state.update {
            it.copy(selectedCount = it.selectedCount.inc())
        }
    }

    private fun decrease() = with(_state.value) {
        if (selectedCount > 1) {
            _state.update {
                it.copy(selectedCount = it.selectedCount.dec())
            }
        }
    }

    private fun init() {
        viewModelScope.launch {
            savedStateHandle.get<Int>("dishId")?.let { dishId ->
                val params = RetrieveDishByIdUseCase.Params.toParams(dishId)
                when (val result = retrieveDishUseCase(params)) {
                    is Result.Success -> _state.update { it.copy(dish = result.data) }
                    is Result.Error -> _state.update { it.copy(error = result.asErrorUiText()) }
                }
            }
        }
    }
}