package com.jerdoul.foody.presentation.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jerdoul.foody.domain.cart.CartCache
import com.jerdoul.foody.domain.error.type.CheckoutError
import com.jerdoul.foody.domain.pojo.Dish
import com.jerdoul.foody.presentation.asUiText
import com.jerdoul.foody.presentation.navigation.Destination
import com.jerdoul.foody.presentation.navigation.Navigator
import com.jerdoul.foody.ui.utils.SnackbarController
import com.jerdoul.foody.ui.utils.SnackbarEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
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

    companion object {
        private const val TAX_PERCENTAGE = .15f
    }

    private val _state = MutableStateFlow(CartState())
    val state: StateFlow<CartState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            cartCache.dishes.collectLatest { cachedDishes ->
                if (cachedDishes.isEmpty()) {
                    _state.update { currentState ->
                        currentState.copy(
                            dishes = emptyList(),
                            dishCounts = emptyList()
                        )
                    }
                } else {
                    val dishCounts = cachedDishes.groupBy { it.id }
                        .mapValues { map -> map.value.size }
                        .map { map -> map.value }
                        .toList()
                    val dishes = cachedDishes.distinctBy { it.id }
                    val itemsPrice = cachedDishes.sumOf { it.price }
                    val totalPrice = itemsPrice + (itemsPrice * TAX_PERCENTAGE)
                    val itemCount = dishCounts.sum()
                    _state.update { currentState ->
                        currentState.copy(
                            dishes = dishes,
                            dishCounts = dishCounts,
                            itemsPrice = itemsPrice,
                            totalPrice = totalPrice,
                            itemCount = itemCount
                        )
                    }
                }
            }
        }
    }

    fun onAction(action: CartAction) {
        when (action) {
            CartAction.Checkout -> checkout()
            is CartAction.Decrease -> decrement(action.id)
            is CartAction.Increase -> increment(action.id)
            is CartAction.Remove -> remove(action.dish)
        }
    }

    private fun checkout() = with(_state.value) {
        viewModelScope.launch {
            _state.update { currentState ->
                currentState.copy(isLoading = true)
            }
            delay(1000L)
            if (dishes.isEmpty()) {
                SnackbarEvent.UiTextMessage(CheckoutError.NO_ITEMS.asUiText()).also {
                    SnackbarController.showEvent(it)
                }
            } else {
                navigator.navigate(Destination.PaymentScreen)
            }

        }
    }

    private fun remove(dish: Dish) {
        viewModelScope.launch {
            cartCache.remove(dish)
        }
    }

    private fun increment(id: Int) {
        viewModelScope.launch {
            _state.value.dishes.firstOrNull { it.id == id }?.let { dish ->
                cartCache.add(dish)
            }
        }
    }

    private fun decrement(id: Int) {
        viewModelScope.launch {
            _state.value.dishes.firstOrNull { it.id == id }?.let { dish ->
                cartCache.remove(dish)
            }
        }
    }
}