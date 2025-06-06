package com.jerdoul.foody.ui.composable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import com.jerdoul.foody.ui.theme.FieldTextColor
import kotlinx.coroutines.launch

@Composable
fun NavToolbar(
    modifier: Modifier,
    title: String? = null,
    onBack: suspend () -> Unit,
    customContent: @Composable () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var animationVisibility by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        animationVisibility = true
    }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        AnimatedVisibility(
            visible = animationVisibility,
            enter = scaleIn(
                animationSpec = tween(
                    durationMillis = 500,
                    delayMillis = 150
                )
            )
        ) {
            IconButton(
                onClick = { coroutineScope.launch { onBack() } }
            ) {
                Icon(
                    painter = rememberVectorPainter(Icons.AutoMirrored.Filled.ArrowBack),
                    contentDescription = null
                )
            }
        }
        title?.let {
            AnimatedVisibility(
                visible = animationVisibility,
                enter = scaleIn(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineMedium,
                    color = FieldTextColor
                )
            }
        }
        AnimatedVisibility(
            visible = animationVisibility,
            enter = scaleIn(
                animationSpec = tween(
                    durationMillis = 500,
                    delayMillis = 150
                )
            )
        ) {
            customContent()
        }
    }
}