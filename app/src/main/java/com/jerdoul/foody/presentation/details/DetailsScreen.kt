package com.jerdoul.foody.presentation.details

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.jerdoul.foody.R
import com.jerdoul.foody.domain.pojo.Dish
import com.jerdoul.foody.domain.pojo.identifier
import com.jerdoul.foody.presentation.details.composables.CardContent
import com.jerdoul.foody.presentation.details.composables.Card
import com.jerdoul.foody.presentation.navigation.Destination
import com.jerdoul.foody.presentation.navigation.Navigator
import com.jerdoul.foody.ui.composable.Counter
import com.jerdoul.foody.ui.composable.IconWithCounter
import com.jerdoul.foody.ui.composable.NavToolbar
import com.jerdoul.foody.utils.extensions.verticalSlideInAnimation
import kotlinx.coroutines.launch

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.DetailsScreen(
    navigator: Navigator,
    state: DetailsState,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onAction: (DetailsAction) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val screenHeightPx = LocalWindowInfo.current.containerSize.height.toFloat()

    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (toolbarRef, dishImageRef, detailsRef, addToCartRef, fabRef) = createRefs()
        NavToolbar(
            modifier = Modifier
                .constrainAs(toolbarRef) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start, 14.dp)
                    end.linkTo(parent.end, 14.dp)
                    width = Dimension.fillToConstraints
                }
                .statusBarsPadding(),
            onBack = { coroutineScope.launch { navigator.navigateUp() } },
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
        Image(
            modifier = Modifier
                .constrainAs(dishImageRef) {
                    top.linkTo(toolbarRef.bottom, 24.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    height = Dimension.value(400.dp)
                    width = Dimension.value(400.dp)
                }
                .sharedElement(
                    sharedContentState = rememberSharedContentState(key = state.dish.identifier()),
                    animatedVisibilityScope = animatedVisibilityScope,
                    boundsTransform = { _, _ ->
                        tween(durationMillis = 800)
                    }
                ),
            painter = painterResource(id = R.drawable.logo_dish),
            contentDescription = null
        )
        state.dish?.let { dish ->
            Card(
                modifier = Modifier
                    .constrainAs(detailsRef) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                        width = Dimension.fillToConstraints
                    }
                    .verticalSlideInAnimation(
                        initialOffsetY = screenHeightPx,
                        playOnce = true,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioLowBouncy,
                            stiffness = Spring.StiffnessLow
                        ),
                        delay = 800
                    ),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                CardContent(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 24.dp,
                            vertical = 48.dp
                        ),
                    dishName = dish.name,
                    dishPrice = dish.price,
                    dishRating = dish.rating,
                    dishCalories = dish.calories,
                    dishCookTime = dish.cookTimeMinutes,
                    dishDescription = dish.description,
                    dishIngredients = dish.ingredients,
                )
            }
        }
        Counter(
            modifier = Modifier
                .constrainAs(addToCartRef) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(detailsRef.top)
                    top.linkTo(detailsRef.top)
                }
                .verticalSlideInAnimation(
                    initialOffsetY = screenHeightPx,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessLow
                    ),
                    delay = 800
                ),
            onDecrement = { onAction(DetailsAction.Decrease) },
            onIncrement = { onAction(DetailsAction.Increase) },
            selectedCount = state.selectedCount
        )
        FloatingActionButton(
            modifier = Modifier
                .constrainAs(fabRef) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom, 24.dp)
                }
                .verticalSlideInAnimation(
                    initialOffsetY = screenHeightPx,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessLow
                    ),
                    delay = 900
                )
                .navigationBarsPadding(),
            onClick = {
                onAction(DetailsAction.AddToCart)
            }
        ) {
            Icon(
                painter = rememberVectorPainter(Icons.Filled.Add),
                contentDescription = null
            )
        }
    }
}