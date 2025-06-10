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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.IntOffset
import com.jerdoul.foody.ui.utils.MultipleEventsCutter
import com.jerdoul.foody.ui.utils.get
import kotlinx.coroutines.delay

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

fun Modifier.verticalSlideInAnimation(
    initialOffsetY: Float,
    enabled: Boolean = true,
    playOnce: Boolean = false,
    animationSpec: AnimationSpec<Float> = spring(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = 100f,
    ),
    delay: Long = 0,
    onFinished: () -> Unit = {}
): Modifier = composed {
    val offsetY = remember { Animatable(initialOffsetY) }
    var enableAnimation by rememberSaveable { mutableStateOf(enabled) }

    LaunchedEffect(Unit) {
        if (enableAnimation) {
            delay(delay)
            offsetY.animateTo(
                targetValue = 0f,
                animationSpec = animationSpec
            )
        }
        if (playOnce) {
            enableAnimation = false
        }
        onFinished()
    }

    thenIf(enableAnimation) {
        Modifier.offset { IntOffset(0, offsetY.value.toInt()) }
    }
}

@Composable
fun Modifier.horizontalSlideInAnimation(
    initialOffsetX: Float,
    enabled: Boolean = true,
    playOnce: Boolean = false,
    animationSpec: AnimationSpec<Float> = spring(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = 100f,
    ),
    delay: Long = 0,
    onFinished: () -> Unit = {}
): Modifier = composed {
    val offsetX = remember { Animatable(initialOffsetX) }
    var enableAnimation by rememberSaveable { mutableStateOf(enabled) }

    LaunchedEffect(Unit) {
        if (enableAnimation) {
            delay(delay)
            offsetX.animateTo(
                targetValue = 0f,
                animationSpec = animationSpec
            )
        }
        if (playOnce) {
            enableAnimation = false
        }
        onFinished()
    }

    thenIf(enableAnimation) {
        Modifier.offset { IntOffset(offsetX.value.toInt(), 0) }
    }
}

@Composable
fun Modifier.fadeInAnimation(
    alphaDuration: Int = 500,
    delay: Long = 0
): Modifier {
    val alphaAnimation = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        delay(delay)
        alphaAnimation.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = alphaDuration)
        )
    }

    return this
        .graphicsLayer {
            alpha = alphaAnimation.value
        }
}

fun Modifier.thenIf(condition: Boolean, modifier: Modifier.() -> Modifier): Modifier {
    return if (condition) {
        this.modifier()
    } else {
        this
    }
}