package com.jerdoul.foody.presentation.details

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.VectorPainter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.jerdoul.foody.R
import com.jerdoul.foody.domain.pojo.Dish
import com.jerdoul.foody.domain.pojo.IngredientType
import com.jerdoul.foody.domain.pojo.identifier
import com.jerdoul.foody.presentation.navigation.Destination
import com.jerdoul.foody.presentation.navigation.Navigator
import com.jerdoul.foody.ui.composable.Counter
import com.jerdoul.foody.ui.composable.IconWithCounter
import com.jerdoul.foody.ui.composable.NavToolbar
import com.jerdoul.foody.ui.theme.FieldTextColor
import com.jerdoul.foody.ui.theme.FieldTextHintColor
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
        val (toolbarRef, dishImageRef, detailsRef, addToCartRef) = createRefs()
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
                .clip(CircleShape)
                .sharedElement(
                    sharedContentState = rememberSharedContentState(key = "image/${state.dish.identifier()}"),
                    animatedVisibilityScope = animatedVisibilityScope,
                    boundsTransform = { _, _ ->
                        tween(durationMillis = 1000)
                    }
                ),
            painter = painterResource(id = R.drawable.logo_dish),
            contentDescription = "Dish Item"
        )
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
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessLow
                    ),
                    delay = 800
                ),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
        ) {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                val (nameRef, priceRef, infoRef, descriptionRef, ingredientsRef, fabRef) = createRefs()
                Text(
                    modifier = Modifier.constrainAs(nameRef) {
                        top.linkTo(parent.top, 24.dp)
                        start.linkTo(parent.start)
                        end.linkTo(priceRef.start)
                        width = Dimension.fillToConstraints
                    },
                    text = state.dish?.name ?: "",
                    style = MaterialTheme.typography.headlineLarge,
                    color = FieldTextColor
                )
                Row(
                    modifier = Modifier.constrainAs(priceRef) {
                        end.linkTo(parent.end)
                        bottom.linkTo(nameRef.bottom)
                        width = Dimension.fillToConstraints
                    },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "$",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = state.dish?.price ?: "0.00",
                        style = MaterialTheme.typography.headlineLarge,
                        color = FieldTextColor
                    )
                }
                InfoContent(
                    modifier = Modifier.constrainAs(infoRef) {
                        top.linkTo(priceRef.bottom, 24.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        width = Dimension.fillToConstraints
                    },
                    dish = state.dish
                )
                DetailsContent(
                    modifier = Modifier.constrainAs(descriptionRef) {
                        top.linkTo(infoRef.bottom, 24.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        width = Dimension.fillToConstraints
                    },
                    dish = state.dish
                )
                IngredientsContent(
                    modifier = Modifier
                        .constrainAs(ingredientsRef) {
                            top.linkTo(descriptionRef.bottom, 24.dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            width = Dimension.fillToConstraints
                        }
                        .navigationBarsPadding(),
                    dish = state.dish
                )
                FloatingActionButton(
                    modifier = Modifier
                        .constrainAs(fabRef) {
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom)
                        }
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
    }
}


@Composable
fun InfoContent(modifier: Modifier, dish: Dish?) {
    Row(modifier = modifier) {
        CharacteristicItem(
            title = dish?.rating ?: "0",
            vectorPainter = rememberVectorPainter(Icons.Filled.Star)
        )
        CharacteristicItem(
            modifier = Modifier.weight(1f),
            title = "${dish?.calories ?: 0} Calories",
            vectorPainter = rememberVectorPainter(Icons.Filled.Info)
        )
        CharacteristicItem(
            title = dish?.cookTimeMinutes ?: "0 min",
            vectorPainter = rememberVectorPainter(Icons.Filled.Notifications)
        )
    }
}

@Composable
fun DetailsContent(modifier: Modifier, dish: Dish?) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = stringResource(R.string.details),
            style = MaterialTheme.typography.headlineMedium,
            color = FieldTextColor
        )
        Text(
            text = dish?.description ?: "",
            style = MaterialTheme.typography.bodySmall,
            lineHeight = 24.sp,
            color = FieldTextHintColor
        )
    }
}

@Composable
fun IngredientsContent(modifier: Modifier, dish: Dish?) {
    dish?.ingredients?.let { ingredients ->
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = stringResource(R.string.ingredients),
                style = MaterialTheme.typography.headlineMedium,
                color = FieldTextColor
            )
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(ingredients) { ingredient ->
                    val painter = remember { ingredient.toDrawable() }
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .background(
                                color = MaterialTheme.colorScheme.background,
                                shape = MaterialTheme.shapes.small
                            )
                    ) {
                        Image(
                            painter = painterResource(painter),
                            contentDescription = null
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CharacteristicItem(
    modifier: Modifier = Modifier,
    title: String,
    vectorPainter: VectorPainter
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = vectorPainter,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onPrimary
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            color = FieldTextColor
        )
    }
}

fun IngredientType.toDrawable() = when (this) {
    IngredientType.VEGETABLE,
    IngredientType.FRUIT,
    IngredientType.MEAT,
    IngredientType.SEAFOOD,
    IngredientType.DAIRY,
    IngredientType.GRAIN,
    IngredientType.NUT,
    IngredientType.LEGUME,
    IngredientType.SPICE,
    IngredientType.HERB,
    IngredientType.SWEETENER,
    IngredientType.OIL,
    IngredientType.SAUCE,
    IngredientType.EGG,
    IngredientType.BEVERAGE,
    IngredientType.MUSHROOM,
    IngredientType.PLANT_BASED,
    IngredientType.PROCESSED,
    IngredientType.OTHER -> R.drawable.steak
}