package com.jerdoul.foody.presentation.dashboard.composables

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jerdoul.foody.domain.pojo.Dish

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.DishesContent(
    modifier: Modifier,
    dishes: List<Dish>,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onDishSelected: (Int) -> Unit
) {
    val listState = rememberLazyListState()
    val flingBehavior = rememberSnapFlingBehavior(lazyListState = listState)

    LazyRow(
        modifier = modifier,
        state = listState,
        flingBehavior = flingBehavior,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(dishes) { dish ->
            DishItem(
                dish = dish,
                listState = listState,
                animatedVisibilityScope = animatedVisibilityScope,
                onDishSelected = {
                    onDishSelected(dish.id)
                }
            )
        }
    }
}

