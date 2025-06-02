package com.jerdoul.foody.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jerdoul.foody.domain.Result
import com.jerdoul.foody.domain.pojo.Dish
import com.jerdoul.foody.domain.pojo.DishType
import com.jerdoul.foody.domain.usecase.RetrieveDishTypesUseCase
import com.jerdoul.foody.domain.usecase.RetrieveDishesUseCase
import com.jerdoul.foody.presentation.asErrorUiText
import com.jerdoul.foody.presentation.navigation.Destination
import com.jerdoul.foody.presentation.navigation.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val retrieveDishTypesUseCase: RetrieveDishTypesUseCase,
    private val retrieveDishesUseCase: RetrieveDishesUseCase,
    private val navigator: Navigator
) : ViewModel() {

    private var allDishes: List<Dish> = emptyList()

    private val _searchQuery = MutableStateFlow("")

    private val _state = MutableStateFlow(DashboardState())
    val state = _state.onStart {
        onAction(DashboardAction.RetrieveData)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = DashboardState()
    )

    init {
        viewModelScope.launch {
            _searchQuery
                .onEach { query ->_state.update { it.copy(searchQuery = query) } }
                .debounce(500)
                .filter { it.isNotBlank() }
                .collect {
                    navigator.navigate(Destination.SearchScreen(searchQuery = it))
                }
        }
    }

    fun onAction(action: DashboardAction) {
        when (action) {
            DashboardAction.RetrieveData -> retrieveData()
            is DashboardAction.Search -> search(action.query)
            is DashboardAction.FilterDishes -> filterDishes(action.dishType)
        }
    }

    private fun filterDishes(dishType: DishType?) {
        _state.update { oldState ->
            val dishes = dishType?.let { dishType ->
                allDishes.filter { dish -> dish.type == dishType }
            } ?: allDishes
            oldState.copy(dishes = dishes)
        }
    }

    private fun search(query: String) {
        _searchQuery.update { query }
    }

    private fun retrieveData() {
        viewModelScope.launch {
            val dishTypeJob = async { retrieveDishTypesUseCase() }
            val dishesJob = async { retrieveDishesUseCase() }
            val dishTypesResult = dishTypeJob.await()
            val dishesResult = dishesJob.await()

            if (dishTypesResult is Result.Success && dishesResult is Result.Success) {
                allDishes = dishesResult.data
                _state.update {
                    it.copy(
                        dishTypes = dishTypesResult.data,
                        dishes = dishesResult.data,
                        isLoading = false
                    )
                }
            } else {
                val error = when {
                    dishTypesResult is Result.Error -> dishTypesResult.asErrorUiText()
                    dishesResult is Result.Error -> dishesResult.asErrorUiText()
                    else -> null
                }
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = error
                    )
                }

            }
        }
    }
}