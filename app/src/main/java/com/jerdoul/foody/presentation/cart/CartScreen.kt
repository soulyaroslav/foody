package com.jerdoul.foody.presentation.cart

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.jerdoul.foody.R
import com.jerdoul.foody.presentation.cart.composables.BottomCartContent
import com.jerdoul.foody.presentation.cart.composables.CartItem
import com.jerdoul.foody.presentation.navigation.Navigator
import com.jerdoul.foody.ui.composable.NavToolbar
import com.jerdoul.foody.ui.theme.FieldTextColor
import com.jerdoul.foody.utils.extensions.verticalSlideInAnimation
import kotlinx.coroutines.launch

@Composable
fun CartScreen(
    navigator: Navigator,
    state: CartState,
    onAction: (CartAction) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val windowInfo = LocalWindowInfo.current
    val screenHeightPx = remember { windowInfo.containerSize.height.toFloat() }

    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (toolbarRef, dishesRef, paymentRef, noItemsRef) = createRefs()
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
            title = stringResource(R.string.cart_food),
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
        if (state.dishes.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .constrainAs(dishesRef) {
                        top.linkTo(toolbarRef.bottom, 24.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(paymentRef.top)
                        width = Dimension.fillToConstraints
                        height = Dimension.fillToConstraints
                    },
                verticalArrangement = Arrangement.spacedBy(14.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                contentPadding = PaddingValues(24.dp)
            ) {
                itemsIndexed(
                    items = state.dishes,
                    key = { _, dish -> dish.id }
                ) { position, dish ->
                    CartItem(
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateItem(),
                        dish = dish,
                        onIncrement = {
                            onAction(CartAction.Increase(it))
                        },
                        onDecrement = {
                            onAction(CartAction.Decrease(it))
                        },
                        onRemove = {
                            onAction(CartAction.Remove(it))
                        },
                        selectedCount = state.dishCounts[position]
                    )
                }
            }

            BottomCartContent(
                modifier = Modifier
                    .constrainAs(paymentRef) {
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
                isLoading = state.isLoading,
                itemCount = state.itemCount,
                itemsPrice = state.itemsPrice,
                totalPrice = state.totalPrice,
                onCheckout = {
                    onAction(CartAction.Checkout)
                }
            )
        } else {
            Text(
                modifier = Modifier
                    .constrainAs(noItemsRef) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                        top.linkTo(toolbarRef.bottom)
                    },
                text = stringResource(R.string.cart_is_empty),
                style = MaterialTheme.typography.headlineLarge,
                color = FieldTextColor
            )
        }
    }
}