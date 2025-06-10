package com.jerdoul.foody.presentation.search

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jerdoul.foody.R
import com.jerdoul.foody.presentation.dashboard.composables.SearchBar
import com.jerdoul.foody.presentation.navigation.Destination
import com.jerdoul.foody.presentation.navigation.Navigator
import com.jerdoul.foody.presentation.search.composables.SearchDishItem
import com.jerdoul.foody.ui.composable.IconWithCounter
import com.jerdoul.foody.ui.composable.NavToolbar
import com.jerdoul.foody.ui.theme.FieldTextColor
import com.jerdoul.foody.utils.extensions.horizontalSlideInAnimation
import com.jerdoul.foody.utils.extensions.verticalSlideInAnimation
import kotlinx.coroutines.launch

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.SearchScreen(
    navigator: Navigator,
    state: SearchState,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onAction: (SearchAction) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val windowInfo = LocalWindowInfo.current
    val screenHeightPx = remember { windowInfo.containerSize.height.toFloat() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
    ) {
        NavToolbar(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding(),
            title = stringResource(R.string.search_food),
            onBack = { navigator.navigateUp() },
            customContent = {
                IconWithCounter(
                    count = state.cartCount,
                    painter = rememberVectorPainter(Icons.Filled.ShoppingCart),
                    onShopCartSelected = {
                        coroutineScope.launch {
                            navigator.navigate(Destination.CartScreen)
                        }
                    }
                )
            }
        )
        SearchBar(
            modifier = Modifier
                .fillMaxWidth()
                .verticalSlideInAnimation(
                    initialOffsetY = -300f,
                    animationSpec = tween(
                        durationMillis = 400,
                        easing = FastOutSlowInEasing
                    )
                ),
            query = state.searchQuery,
            placeholder = stringResource(R.string.search_food_placeholder),
            onQueryChange = { query ->
                onAction(SearchAction.Search(query))
            }
        )

        LazyVerticalStaggeredGrid(
            modifier = Modifier
                .fillMaxSize()
                .verticalSlideInAnimation(
                    initialOffsetY = screenHeightPx,
                    enabled = true,
                    playOnce = true,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessLow
                    ),
                    delay = 200,
                ),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalItemSpacing = 14.dp,
            columns = StaggeredGridCells.Fixed(2),
        ) {
            item {
                Text(
                    modifier = Modifier.horizontalSlideInAnimation(
                        initialOffsetX = -400f,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioLowBouncy,
                            stiffness = Spring.StiffnessLow
                        ),
                        delay = 600
                    ),
                    text = stringResource(R.string.found_results, state.dishesCount),
                    style = MaterialTheme.typography.displayLarge,
                    color = FieldTextColor
                )
            }
            items(state.dishes) { dish ->
                SearchDishItem(
                    dish = dish,
                    animatedVisibilityScope = animatedVisibilityScope,
                    onDishSelected = {
                        coroutineScope.launch {
                            navigator.navigate(Destination.DetailsScreen(it.id))
                        }
                    }
                )
            }
        }
    }
}

