package com.jerdoul.foody.presentation.cart

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.jerdoul.foody.R
import com.jerdoul.foody.domain.pojo.Dish
import com.jerdoul.foody.presentation.navigation.Navigator
import com.jerdoul.foody.ui.composable.Counter
import com.jerdoul.foody.ui.composable.NavToolbar
import com.jerdoul.foody.ui.theme.FieldTextColor
import com.jerdoul.foody.ui.theme.FieldTextHintColor
import kotlinx.coroutines.launch

@Composable
fun CartScreen(
    navigator: Navigator,
    state: CartState,
    onAction: (CartAction) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val screenHeightPx = LocalWindowInfo.current.containerSize.height.toFloat()

    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (toolbarRef, dishesRef) = createRefs()
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
            title = "Cart Food",
            customContent = {
                Box(
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = MaterialTheme.shapes.small
                        )
                        .padding(14.dp)
                ) {
                    Text(
                        text = "YH",
                        style = MaterialTheme.typography.headlineSmall,
                        color = FieldTextColor
                    )
                }
            }
        )
        LazyColumn(
            modifier = Modifier
                .constrainAs(dishesRef) {
                    top.linkTo(toolbarRef.bottom, 24.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                },
            verticalArrangement = Arrangement.spacedBy(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(24.dp)
        ) {
            items(state.dishStates) { dishState ->
                CartItem(
                    modifier = Modifier.fillMaxWidth(),
                    dish = dishState.dish,
                    onIncrement = {
                        onAction(CartAction.Increase(it))
                    },
                    onDecrement = {
                        onAction(CartAction.Decrease(it))
                    },
                    onRemove = { dish, _ ->
                        onAction(CartAction.Remove(dish))
                    },
                    selectedCount = dishState.selectedCount
                )
            }

        }
    }
}

@Composable
fun CartItem(
    modifier: Modifier,
    dish: Dish,
    onIncrement: (Int) -> Unit,
    onDecrement: (Int) -> Unit,
    onRemove: (Dish, Int) -> Unit,
    selectedCount: Int
) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            when (it) {
                SwipeToDismissBoxValue.StartToEnd -> {
                    return@rememberSwipeToDismissBoxState false
                }

                SwipeToDismissBoxValue.EndToStart -> {
                    onRemove(dish, selectedCount)
                }

                SwipeToDismissBoxValue.Settled -> {
                    return@rememberSwipeToDismissBoxState false
                }
            }
            return@rememberSwipeToDismissBoxState true
        },
        positionalThreshold = { distance ->
            distance * .15f
        }
    )
    SwipeToDismissBox(
        modifier = modifier,
        state = dismissState,
        enableDismissFromStartToEnd = false,
        backgroundContent = { SwipeToDeleteBackground() },
        content = {
            SwipeContent(
                dish = dish,
                onIncrement = {
                    onIncrement(dish.id)
                },
                onDecrement = {
                    onDecrement(dish.id)
                },
                selectedCount = selectedCount
            )
        }
    )
}

@Composable
fun SwipeContent(
    dish: Dish,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    selectedCount: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            val (imageRef, titleRef, descriptionRef, priceRef, counterRef) = createRefs()
            Image(
                modifier = Modifier
                    .constrainAs(imageRef) {
                        top.linkTo(parent.top, 14.dp)
                        start.linkTo(parent.start)
                        bottom.linkTo(parent.bottom, 14.dp)
                    }
                    .size(150.dp)
                    .clip(CircleShape),
                painter = painterResource(id = R.drawable.logo_dish),
                contentDescription = null
            )
            Text(
                modifier = Modifier.constrainAs(titleRef) {
                    top.linkTo(parent.top)
                    start.linkTo(imageRef.end, 24.dp)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                },
                text = dish.name,
                style = MaterialTheme.typography.headlineSmall,
                color = FieldTextColor
            )
            Text(
                modifier = Modifier.constrainAs(descriptionRef) {
                    top.linkTo(titleRef.bottom, 8.dp)
                    start.linkTo(titleRef.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                },
                text = dish.shortDescription,
                style = MaterialTheme.typography.bodySmall,
                color = FieldTextHintColor
            )
            Row(
                modifier = Modifier
                    .constrainAs(priceRef) {
                        top.linkTo(descriptionRef.bottom, 8.dp)
                        start.linkTo(descriptionRef.start)
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
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
            Counter(
                modifier = Modifier
                    .constrainAs(counterRef) {
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    },
                onDecrement = onDecrement,
                onIncrement = onIncrement,
                selectedCount = selectedCount
            )
        }
    }
}

@Composable
fun SwipeToDeleteBackground() {
    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = {}
        ) {
            Icon(
                painter = rememberVectorPainter(Icons.Default.Delete),
                contentDescription = null
            )
        }
    }
}