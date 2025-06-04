package com.jerdoul.foody.presentation.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jerdoul.foody.domain.Result
import com.jerdoul.foody.domain.pojo.Dish
import com.jerdoul.foody.domain.usecase.RetrieveDishesUseCase
import com.jerdoul.foody.presentation.asErrorUiText
import com.jerdoul.foody.presentation.navigation.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val retrieveDishesUseCase: RetrieveDishesUseCase,
    private val navigator: Navigator,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var allDishes: List<Dish> = emptyList()

    private val _searchQuery = MutableStateFlow("")

    private val _state = MutableStateFlow(SearchState())
    val state = _state.onStart {
        onAction(SearchAction.RetrieveData)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = SearchState()
    )

    init {
        initSearching()
    }

    fun onAction(action: SearchAction) {
        when (action) {
            SearchAction.RetrieveData -> retrieveData()
            is SearchAction.Search -> search(action.query)
        }
    }

    private fun initSearching() {
        viewModelScope.launch {
            _searchQuery
                .onEach { query -> _state.update { it.copy(searchQuery = query) } }
                .debounce(500)
                .collect { query ->
                    if (query.isNotEmpty()) {
                        val searchedDishes = allDishes.filter { dish -> dish.name.contains(query, true) }
                        _state.update {
                            it.copy(
                                dishes = searchedDishes,
                                dishesCount = searchedDishes.size
                            )
                        }
                    } else {
                        _state.update {
                            it.copy(
                                dishes = allDishes,
                                dishesCount = allDishes.size
                            )
                        }
                    }
                }
        }
        savedStateHandle.get<String>("searchQuery")?.let { query ->
            if (query.isNotEmpty()) {
                _searchQuery.update { query }
            }
        }
    }

    private fun search(query: String) {
        _searchQuery.update { query }
    }

    private fun retrieveData() {
        viewModelScope.launch {
            when (val dishesResult = retrieveDishesUseCase()) {
                is Result.Success -> {
                    allDishes = dishesResult.data
                    _state.update {
                        it.copy(isLoading = false)
                    }
                }

                is Result.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = dishesResult.asErrorUiText()
                        )
                    }
                }
            }
        }
    }
}