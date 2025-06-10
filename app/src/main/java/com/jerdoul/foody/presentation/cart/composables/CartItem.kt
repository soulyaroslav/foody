package com.jerdoul.foody.presentation.cart.composables

import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jerdoul.foody.domain.pojo.Dish

@Composable
fun CartItem(
    modifier: Modifier,
    dish: Dish,
    onIncrement: (Int) -> Unit,
    onDecrement: (Int) -> Unit,
    onRemove: (Dish) -> Unit,
    selectedCount: Int
) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            when (it) {
                SwipeToDismissBoxValue.StartToEnd -> {
                    return@rememberSwipeToDismissBoxState false
                }

                SwipeToDismissBoxValue.EndToStart -> {
                    onRemove(dish)
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
        backgroundContent = {
            SwipeToDeleteBackground()
                            },
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