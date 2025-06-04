package com.jerdoul.foody.presentation.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.jerdoul.foody.R
import com.jerdoul.foody.domain.pojo.Dish
import com.jerdoul.foody.presentation.dashboard.SearchBar
import com.jerdoul.foody.presentation.navigation.Destination
import com.jerdoul.foody.presentation.navigation.Navigator
import com.jerdoul.foody.ui.theme.FieldTextColor
import com.jerdoul.foody.ui.theme.FieldTextHintColor
import com.jerdoul.foody.ui.theme.OnError
import com.jerdoul.foody.utils.extensions.clickableSingle
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
    val configuration = LocalConfiguration.current
    val screenHeightPx = with(LocalDensity.current) {
        configuration.screenHeightDp.dp.toPx()
    }

    var enableVerticalSlideAnimation by rememberSaveable { mutableStateOf(true) }

    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (searchRef, toolbarRef, gridRef) = createRefs()
        SearchToolbar(
            modifier = Modifier
                .constrainAs(toolbarRef) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start, 14.dp)
                    end.linkTo(parent.end, 14.dp)
                    width = Dimension.fillToConstraints
                }
                .statusBarsPadding(),
            title = stringResource(R.string.search_food),
            onBack = { coroutineScope.launch { navigator.navigateUp() } },
            onShopCartSelected = {}
        )
        SearchBar(
            modifier = Modifier
                .constrainAs(searchRef) {
                    top.linkTo(toolbarRef.bottom, 24.dp)
                    start.linkTo(parent.start, 24.dp)
                    end.linkTo(parent.end, 24.dp)
                    width = Dimension.fillToConstraints
                }
                .verticalSlideInAnimation(
                    initialOffsetY = -300f,
                    animationSpec = tween(
                        durationMillis = 400,
                        easing = FastOutSlowInEasing
                    )
                ),
            query = state.searchQuery,
            placeholder = "Search food",
            onQueryChange = { query ->
                onAction(SearchAction.Search(query))
            }
        )

        LazyVerticalStaggeredGrid(
            modifier = Modifier
                .constrainAs(gridRef) {
                    top.linkTo(searchRef.bottom, 24.dp)
                    start.linkTo(parent.start, 24.dp)
                    end.linkTo(parent.end, 24.dp)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }
                .verticalSlideInAnimation(
                    initialOffsetY = screenHeightPx,
                    enabled = enableVerticalSlideAnimation,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessLow
                    ),
                    delay = 200,
                    onFinished = {
                        enableVerticalSlideAnimation = false
                    }
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
                    text = "Found\n${state.dishesCount} results",
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

@Composable
fun SearchToolbar(
    modifier: Modifier,
    title: String,
    onBack: () -> Unit,
    onShopCartSelected: () -> Unit
) {
    var animate by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        animate = true
    }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        AnimatedVisibility(
            visible = animate,
            enter = scaleIn(
                animationSpec = tween(
                    durationMillis = 500,
                    delayMillis = 150
                )
            )
        ) {
            IconButton(
                onClick = { onBack() }
            ) {
                Icon(
                    painter = rememberVectorPainter(Icons.AutoMirrored.Filled.ArrowBack),
                    contentDescription = "Arrow Back"
                )
            }
        }
        AnimatedVisibility(
            visible = animate,
            enter = scaleIn(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium,
                color = FieldTextColor
            )
        }
        AnimatedVisibility(
            visible = animate,
            enter = scaleIn(
                animationSpec = tween(
                    durationMillis = 500,
                    delayMillis = 150
                )
            )
        ) {
            IconButton(
                onClick = { onShopCartSelected() }
            ) {
                Icon(
                    painter = rememberVectorPainter(Icons.Filled.ShoppingCart),
                    contentDescription = "Shopping Cart"
                )
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.SearchDishItem(
    dish: Dish,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onDishSelected: (Dish) -> Unit
) {
    Column(
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(14.dp)
            )
            .clickableSingle { onDishSelected(dish) }
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier
                .size(200.dp)
                .clip(CircleShape)
                .sharedElement(
                    sharedContentState = rememberSharedContentState(key = "image/logo_dish_${dish.id}"),
                    animatedVisibilityScope = animatedVisibilityScope,
                    boundsTransform = { _, _ ->
                        tween(durationMillis = 1000)
                    }
                ),
            painter = painterResource(id = R.drawable.logo_dish),
            contentDescription = "Dish Item"
        )
        Text(
            text = dish.name,
            style = MaterialTheme.typography.headlineMedium,
            color = FieldTextColor
        )
        Text(
            text = dish.description,
            style = MaterialTheme.typography.bodySmall,
            color = FieldTextHintColor
        )
        Text(
            text = stringResource(R.string.calories, dish.calories),
            style = MaterialTheme.typography.bodySmall,
            color = OnError
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "$",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = dish.price,
                style = MaterialTheme.typography.headlineLarge,
                color = FieldTextColor
            )
        }
        Spacer(modifier = Modifier.height(14.dp))
    }
}