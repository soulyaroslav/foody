package com.jerdoul.foody.presentation.dashboard

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.jerdoul.foody.R
import com.jerdoul.foody.domain.pojo.Dish
import com.jerdoul.foody.domain.pojo.DishType
import com.jerdoul.foody.presentation.navigation.Navigator
import com.jerdoul.foody.ui.theme.FieldTextColor
import com.jerdoul.foody.ui.theme.FieldTextHintColor
import com.jerdoul.foody.ui.theme.OnError
import com.jerdoul.foody.utils.extensions.clickableSingle
import com.jerdoul.foody.utils.extensions.horizontalSlideInAnimation
import com.jerdoul.foody.utils.extensions.verticalSlideInAnimation
import kotlinx.coroutines.delay
import kotlin.math.abs
import kotlin.math.min

@Composable
fun DashboardScreen(
    navigator: Navigator,
    state: DashboardState,
    onAction: (DashboardAction) -> Unit
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val (toolbarRef, titleRef, searchRef, typesRef, foodsRef) = createRefs()
        Toolbar(
            modifier = Modifier
                .constrainAs(ref = toolbarRef) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start, 24.dp)
                    end.linkTo(parent.end, 24.dp)
                    width = Dimension.fillToConstraints
                }
                .statusBarsPadding()
        )
        Text(
            modifier = Modifier
                .constrainAs(titleRef) {
                    top.linkTo(toolbarRef.bottom, 24.dp)
                    start.linkTo(parent.start, 24.dp)
                    end.linkTo(parent.end, 24.dp)
                    width = Dimension.fillToConstraints
                }
                .verticalSlideInAnimation(
                    initialOffsetY = -300f,
                    animationSpec = tween(
                        durationMillis = 800,
                        easing = FastOutSlowInEasing
                    )
                ),
            text = "Let's eat\nQuality food",
            style = MaterialTheme.typography.displayLarge,
            color = FieldTextColor
        )
        SearchBar(
            modifier = Modifier
                .constrainAs(ref = searchRef) {
                    top.linkTo(titleRef.bottom, 24.dp)
                    start.linkTo(parent.start, 24.dp)
                    end.linkTo(parent.end, 24.dp)
                    width = Dimension.fillToConstraints
                }
                .verticalSlideInAnimation(
                    initialOffsetY = -300f,
                    animationSpec = tween(
                        durationMillis = 800,
                        easing = FastOutSlowInEasing
                    )
                ),
            query = state.searchQuery,
            placeholder = "Search",
            onQueryChange = {
                onAction(DashboardAction.Search(query = it))
            }
        )
        var selectedItemPosition by remember { mutableIntStateOf(-1) }
        LazyRow(
            modifier = Modifier.constrainAs(typesRef) {
                top.linkTo(searchRef.bottom, 24.dp)
                start.linkTo(parent.start, 24.dp)
                end.linkTo(parent.end, 24.dp)
                width = Dimension.fillToConstraints
            },
            horizontalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            itemsIndexed(state.dishTypes) { itemPosition, foodType ->
                DishTypeItem(
                    dishType = foodType,
                    itemPosition = itemPosition,
                    selectedItemPosition = selectedItemPosition,
                    onItemSelected = { dishType, position ->
                        selectedItemPosition = position
                        onAction(DashboardAction.FilterDishes(dishType))
                    }
                )
            }
        }
        val listState = rememberLazyListState()
        val flingBehavior = rememberSnapFlingBehavior(lazyListState = listState)
        LazyRow(
            modifier = Modifier
                .constrainAs(foodsRef) {
                    top.linkTo(typesRef.bottom, 24.dp)
                    start.linkTo(parent.start, 24.dp)
                    end.linkTo(parent.end, 24.dp)
                    width = Dimension.fillToConstraints
                }
                .horizontalSlideInAnimation(
                    initialOffsetX = 1000f,
                    animationSpec = tween(
                        durationMillis = 700,
                        delayMillis = 1000
                    )
                ),
            state = listState,
            flingBehavior = flingBehavior,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            itemsIndexed(state.dishes) { index, dish ->
                DishItem(
                    dish = dish,
                    listState = listState,
                    index = index
                )
            }
        }
    }
}

@Composable
fun DishItem(
    dish: Dish,
    listState: LazyListState,
    index: Int
) {
    val currentItemOffset = remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val visibleItem = layoutInfo.visibleItemsInfo.find { it.index == index }
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
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier
                .size(200.dp)
                .clip(CircleShape),
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

@Composable
fun DishTypeItem(
    dishType: DishType,
    itemPosition: Int,
    selectedItemPosition: Int,
    onItemSelected: (DishType?, Int) -> Unit
) {
    var animate by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        animate = true
    }

    AnimatedVisibility(
        visible = animate,
        enter = scaleIn(
            animationSpec = tween(
                durationMillis = 500,
                delayMillis = itemPosition * 100,
                easing = FastOutSlowInEasing
            )
        )
    ) {
        Row(
            modifier = Modifier
                .then(
                    if (selectedItemPosition == itemPosition) {
                        Modifier.background(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = RoundedCornerShape(16.dp)
                        )
                    } else {
                        Modifier.border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = RoundedCornerShape(14.dp)
                        )
                    }
                )
                .padding(14.dp)
                .clickableSingle {
                    if (selectedItemPosition != itemPosition) {
                        onItemSelected(dishType, itemPosition)
                    } else {
                        onItemSelected(null, -1)
                    }

                },
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier.size(40.dp),
                painter = painterResource(id = R.drawable.logo_dish),
                contentDescription = "Food Type"
            )
            Text(
                text = dishType.type,
                style = MaterialTheme.typography.headlineSmall,
                color = FieldTextColor
            )
        }
    }
}

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    query: String,
    placeholder: String,
    onQueryChange: (String) -> Unit
) {
    Column(modifier = modifier.animateContentSize()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = query,
                onValueChange = onQueryChange,
                placeholder = {
                    Text(
                        text = placeholder,
                        style = MaterialTheme.typography.bodySmall,
                        color = FieldTextHintColor
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search Icon"
                    )
                },
                singleLine = true,
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp),
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedTextColor = FieldTextColor
                )
            )
            Box(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = MaterialTheme.shapes.small
                    )
                    .padding(14.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_settings),
                    contentDescription = "Settings",
                )
            }
        }
    }
}

@Composable
fun Toolbar(modifier: Modifier = Modifier) {
    var animate by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        animate = true
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
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
            IconButton(onClick = {}) {
                Icon(
                    painter = painterResource(R.drawable.ic_menu),
                    contentDescription = "Menu"
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
    }
}