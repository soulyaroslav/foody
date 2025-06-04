package com.jerdoul.foody.presentation.details

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.jerdoul.foody.presentation.navigation.Navigator
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

    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (toolbarRef, dishImageRef) = createRefs()
        DetailsToolbar(
            modifier = Modifier
                .constrainAs(toolbarRef) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start, 14.dp)
                    end.linkTo(parent.end, 14.dp)
                    width = Dimension.fillToConstraints
                }
                .statusBarsPadding(),
            onBack = { coroutineScope.launch { navigator.navigateUp() } },
            onShopCartSelected = {}
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
                    sharedContentState = rememberSharedContentState(key = "image/logo_dish_${state.dish?.id ?: ""}"),
                    animatedVisibilityScope = animatedVisibilityScope,
                    boundsTransform = { _, _ ->
                        tween(durationMillis = 1000)
                    }
                ),
            painter = painterResource(id = R.drawable.logo_dish),
            contentDescription = "Dish Item"
        )
    }
}

@Composable
fun DetailsToolbar(
    modifier: Modifier,
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