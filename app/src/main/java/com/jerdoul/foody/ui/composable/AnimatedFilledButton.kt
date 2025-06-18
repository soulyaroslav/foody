package com.jerdoul.foody.ui.composable

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.TwoWayConverter
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jerdoul.foody.ui.utils.shape.ArcButtonShape
import com.jerdoul.foody.utils.extensions.clickableSingle
import kotlinx.coroutines.launch

@Composable
fun AnimatedFilledButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    contentPadding: PaddingValues = PaddingValues(vertical = 24.dp),
    cornerRadius: Dp = 12.dp,
    targetArchHeight: Dp = 20.dp,
    targetPadding: Dp = 40.dp,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    loadingContent: @Composable RowScope.() -> Unit = {},
    containerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    content: @Composable RowScope.(TextUnit) -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val archHeight = remember { Animatable(0.dp, Dp.VectorConverter) }
    val horizontalPadding = remember { Animatable(0.dp, Dp.VectorConverter) }
    val fontSize = remember { Animatable(16.sp, TextUnit.VectorConverter) }

    var isAnimationReady by remember { mutableStateOf(false) }

    LaunchedEffect(isAnimationReady) {
        if (isAnimationReady) {
            launch {
                archHeight.animateTo(
                    targetValue = targetArchHeight,
                    animationSpec = tween(durationMillis = 300, easing = FastOutLinearInEasing)
                )
            }
            launch {
                fontSize.animateTo(
                    targetValue = 22.sp,
                    animationSpec = tween(durationMillis = 300, easing = FastOutLinearInEasing)
                )
            }
            launch {
                horizontalPadding.animateTo(
                    targetValue = targetPadding,
                    animationSpec = tween(durationMillis = 300, easing = FastOutLinearInEasing)
                )
            }.join()

            launch {
                archHeight.animateTo(
                    targetValue = 0.dp,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioHighBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                )
            }
            launch {
                fontSize.animateTo(
                    targetValue = 16.sp,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                )
            }
            launch {
                horizontalPadding.animateTo(
                    targetValue = 0.dp,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                )
            }.join()

            isAnimationReady = false
        }
    }

    Row(
        modifier = modifier
            .clip(
                shape = ArcButtonShape(
                    cornerRadius = cornerRadius,
                    archHeight = { archHeight.value },
                    horizontalPadding = { horizontalPadding.value }
                )
            )
            .background(
                color = containerColor,
                shape = ArcButtonShape(
                    cornerRadius = cornerRadius,
                    archHeight = { archHeight.value },
                    horizontalPadding = { horizontalPadding.value }
                )
            )
            .clickableSingle(
                enabled = enabled || !isLoading,
                indication = null,
                interactionSource = interactionSource,
                onClick = {
                    if (!isAnimationReady) {
                        isAnimationReady = true
                    }
                    onClick()
                }
            )
            .padding(contentPadding),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        if (isLoading) {
            loadingContent(this)
        } else {
            content(this, fontSize.value)
        }
    }
}

val TextUnit.Companion.VectorConverter: TwoWayConverter<TextUnit, AnimationVector1D>
    get() = SpToVector

private val SpToVector: TwoWayConverter<TextUnit, AnimationVector1D> =
    TwoWayConverter(
        convertToVector = { AnimationVector1D(it.value) },
        convertFromVector = { it.value.sp }
    )