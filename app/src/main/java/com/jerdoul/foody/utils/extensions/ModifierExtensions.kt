package com.jerdoul.foody.utils.extensions

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Indication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.IntOffset
import com.jerdoul.foody.ui.utils.MultipleEventsCutter
import com.jerdoul.foody.ui.utils.get
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun Modifier.clickableSingle(
    enabled: Boolean = true,
    onClickLabel: String? = null,
    role: Role? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    indication: Indication? = null,
    onClick: () -> Unit
): Modifier {
    val eventCutter = remember { MultipleEventsCutter.get() }

    return this.then(
        Modifier.clickable(
            enabled = enabled,
            onClickLabel = onClickLabel,
            role = role,
            interactionSource = interactionSource,
            indication = indication
        ) {
            eventCutter.processEvent(onClick)
        }
    )
}

@Composable
fun Modifier.verticalSlideInAnimation(
    initialOffsetY: Float,
    animationSpec: AnimationSpec<Float> = spring(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = 100f,
    ),
    delay: Long = 0
): Modifier {
    val offsetY = remember { Animatable(initialOffsetY) }

    LaunchedEffect(Unit) {
        delay(delay)
        launch {
            offsetY.animateTo(
                targetValue = 0f,
                animationSpec = animationSpec
            )
        }
    }

    return this
        .then(Modifier.offset { IntOffset(0, offsetY.value.toInt()) })
}

@Composable
fun Modifier.horizontalSlideInAnimation(
    initialOffsetX: Float,
    animationSpec: AnimationSpec<Float> = spring(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = 100f,
    ),
    delay: Long = 0
): Modifier {
    val offsetX = remember { Animatable(initialOffsetX) }

    LaunchedEffect(Unit) {
        delay(delay)
        launch {
            offsetX.animateTo(
                targetValue = 0f,
                animationSpec = animationSpec
            )
        }
    }

    return this
        .then(Modifier.offset { IntOffset(offsetX.value.toInt(), 0) })
}

@Composable
fun Modifier.fadeInAnimation(
    alphaDuration: Int = 500,
    delay: Long = 0
): Modifier {
    val alpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        delay(delay)
        launch {
            alpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = alphaDuration)
            )
        }
    }

    return this
        .alpha(alpha.value)
}