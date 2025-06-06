package com.jerdoul.foody.presentation.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jerdoul.foody.domain.cart.CartCache
import com.jerdoul.foody.domain.pojo.Dish
import com.jerdoul.foody.presentation.navigation.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartCache: CartCache,
    private val navigator: Navigator
) : ViewModel() {

    private val _state = MutableStateFlow(CartState())
    val state: StateFlow<CartState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            cartCache.dishes.collectLatest { dishes ->
                val grouped = dishes.groupingBy { it.id }.eachCount()
                val dishStates = dishes
                    .distinctBy { it.id }
                    .mapNotNull { dish ->
                        grouped[dish.id]?.let { count ->
                            DishState(dish = dish, selectedCount = count)
                        }
                    }

                _state.update { currentState ->
                    currentState.copy(dishStates = dishStates)
                }
            }
        }
    }

    fun onAction(action: CartAction) {
        when (action) {
            CartAction.RetrieveCart -> {}
            is CartAction.Decrease -> decrement(action.id)
            is CartAction.Increase -> increment(action.id)
            is CartAction.Remove -> remove(action.dish)
        }
    }

    private fun remove(dish: Dish) {
        viewModelScope.launch {
            cartCache.remove(dish)
        }
    }

    private fun increment(id: Int) {
        viewModelScope.launch {
            _state.value.dishStates.firstOrNull { it.dish.id == id }?.let { dishState ->
                cartCache.add(dishState.dish)
            }
        }
    }

    private fun decrement(id: Int) {
        viewModelScope.launch {
            _state.value.dishStates.firstOrNull { it.dish.id == id }?.let { dishState ->
                cartCache.remove(dishState.dish)
            }
        }
    }
}