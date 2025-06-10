package com.jerdoul.foody.presentation.dashboard.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.jerdoul.foody.R
import com.jerdoul.foody.ui.theme.FieldTextColor

@Composable
fun Toolbar(modifier: Modifier = Modifier) {
    var animationVisibility by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        animationVisibility = true
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
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
            IconButton(onClick = {}) {
                Icon(
                    painter = painterResource(R.drawable.ic_menu),
                    contentDescription = null
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