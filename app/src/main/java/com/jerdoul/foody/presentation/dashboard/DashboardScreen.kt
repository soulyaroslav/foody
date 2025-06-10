package com.jerdoul.foody.presentation.dashboard

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jerdoul.foody.R
import com.jerdoul.foody.presentation.dashboard.composables.DishTypesContent
import com.jerdoul.foody.presentation.dashboard.composables.DishesContent
import com.jerdoul.foody.presentation.dashboard.composables.SearchBar
import com.jerdoul.foody.presentation.dashboard.composables.Toolbar
import com.jerdoul.foody.presentation.navigation.Destination
import com.jerdoul.foody.presentation.navigation.Navigator
import com.jerdoul.foody.ui.theme.FieldTextColor
import com.jerdoul.foody.utils.extensions.horizontalSlideInAnimation
import com.jerdoul.foody.utils.extensions.verticalSlideInAnimation
import kotlinx.coroutines.launch

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.DashboardScreen(
    navigator: Navigator,
    state: DashboardState,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onAction: (DashboardAction) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val windowInfo = LocalWindowInfo.current
    val screenWidth = remember { windowInfo.containerSize.width.toFloat() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Toolbar(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .verticalSlideInAnimation(
                    initialOffsetY = -300f,
                    animationSpec = tween(
                        durationMillis = 800,
                        easing = FastOutSlowInEasing
                    )
                ),
            text = stringResource(R.string.dashboard_title),
            style = MaterialTheme.typography.displayLarge,
            color = FieldTextColor
        )
        SearchBar(
            modifier = Modifier
                .fillMaxWidth()
                .verticalSlideInAnimation(
                    initialOffsetY = -300f,
                    animationSpec = tween(
                        durationMillis = 800,
                        easing = FastOutSlowInEasing
                    )
                ),
            query = state.searchQuery,
            placeholder = stringResource(R.string.search),
            onQueryChange = {
                onAction(DashboardAction.Search(query = it))
            }
        )
        DishTypesContent(
            modifier = Modifier.fillMaxWidth(),
            dishTypes = state.dishTypes,
            onFilterTypes = { type ->
                onAction(DashboardAction.FilterDishes(type))
            }
        )
        DishesContent(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalSlideInAnimation(
                    initialOffsetX = screenWidth,
                    enabled = true,
                    playOnce = true,
                    animationSpec = tween(
                        durationMillis = 700,
                        delayMillis = 1000
                    )
                ),
            dishes = state.dishes,
            animatedVisibilityScope = animatedVisibilityScope,
            onDishSelected = { id ->
                coroutineScope.launch {
                    navigator.navigate(Destination.DetailsScreen(id))
                }
            }
        )
    }
}
