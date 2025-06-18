package com.jerdoul.foody.presentation.dashboard.composables

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jerdoul.foody.R
import com.jerdoul.foody.domain.pojo.Dish
import com.jerdoul.foody.domain.pojo.identifier
import com.jerdoul.foody.ui.theme.FieldTextColor
import com.jerdoul.foody.ui.theme.FieldTextHintColor
import com.jerdoul.foody.ui.theme.OnError
import com.jerdoul.foody.utils.extensions.clickableSingle
import kotlin.math.abs
import kotlin.math.min

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.DishItem(
    dish: Dish,
    listState: LazyListState,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onDishSelected: (Dish) -> Unit
) {
    val density = LocalDensity.current
    val currentItemOffset = remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val visibleItem = layoutInfo.visibleItemsInfo.find { it.index == dish.id - 1 }
            if (visibleItem != null) {
                val center = layoutInfo.viewportStartOffset + layoutInfo.viewportSize.width / 2
                val itemCenter = visibleItem.offset + visibleItem.size / 2
                val distance = abs(center - itemCenter).toFloat()
                distance / layoutInfo.viewportSize.width
            } else {
                1f
            }
        }
    }

    val scale by animateFloatAsState(
        targetValue = 1f - min(currentItemOffset.value, 1f) * 0.3f,
        label = "ScaleAnim"
    )

    Column(
        modifier = Modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(14.dp)
            )
            .clickableSingle {
                onDishSelected(dish)
            }
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier
                .size(200.dp)
                .sharedElement(
                    sharedContentState = rememberSharedContentState(key = dish.identifier()),
                    animatedVisibilityScope = animatedVisibilityScope,
                    boundsTransform = { _, _ ->
                        tween(durationMillis = 800)
                    }
                ),
            painter = painterResource(id = R.drawable.logo_dish),
            contentDescription = null
        )
        Text(
            text = dish.name,
            style = MaterialTheme.typography.headlineMedium,
            color = FieldTextColor
        )
        Text(
            text = dish.shortDescription,
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
                text = "${dish.price}",
                style = MaterialTheme.typography.headlineLarge,
                color = FieldTextColor
            )
        }
        Spacer(modifier = Modifier.height(14.dp))
    }
}